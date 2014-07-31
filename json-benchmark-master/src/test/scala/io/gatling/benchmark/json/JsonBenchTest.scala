package io.gatling.benchmark.json

import org.scalameter.api._

object RangeBenchmark
	extends PerformanceTest.Quickbenchmark {

	override lazy val executor = LocalExecutor(
		new Executor.Warmer.Default,
		Aggregator.min,
		new Measurer.Default)

	def readFile(filename: String) = {
		val is = Thread.currentThread().getContextClassLoader().getResourceAsStream(filename)
		scala.io.Source.fromInputStream(is).mkString
	}

	val jsonFiles = Gen.enumeration("jsons")("goessner.json", "twitter_search.json", "citm_catalog.json")
	val jsonDatas = for (f <- jsonFiles) yield readFile(f)

	import com.fasterxml.jackson.databind.ObjectMapper
	val mapper = new ObjectMapper

	import com.google.gson.Gson
	import com.google.gson.{ JsonParser => GsonParser }
	val gson = new Gson
	val parser = new GsonParser

	performance of "Json Parsing" in {
		measure method "Spray" in {
			import spray.json._
			using(jsonDatas) in {
				_.asJson
			}
		}

		measure method "Lift" in {
			import net.liftweb.json._
			using(jsonDatas) in {
				parse
			}
		}

		measure method "Jackson-Object" in {

			using(jsonDatas) in {
				mapper.readValue(_, classOf[Object])
			}
		}

		measure method "Jackson-Ast" in {
			using(jsonDatas) in {
				mapper.readTree
			}
		}
		//
		measure method "Json-Smart" in {
			import net.minidev.json.JSONValue

			using(jsonDatas) in {
				JSONValue.parse
			}
		}

		measure method "GSON-Object" in {
			using(jsonDatas) in {
				gson.fromJson(_, classOf[Object])
			}
		}

		measure method "GSON-Ast" in {
			using(jsonDatas) in {
				parser.parse
			}
		}

		// see https://github.com/jjenkov/parsers-in-java and http://www.infoq.com/articles/HIgh-Performance-Parsers-in-Java
		measure method "Jenkov" in {
			import com.jenkov.parsers.core._
			import com.jenkov.parsers.json._

			using(jsonDatas) in { data =>
				val dataBuffer = new DataCharBuffer(data.toCharArray)
				dataBuffer.length = dataBuffer.data.length
				val elementBuffer = new IndexBuffer(dataBuffer.data.length, true)

				val jenkovParser = new JsonParser

				jenkovParser.parse(dataBuffer, elementBuffer)
			}
		}
	}
}
