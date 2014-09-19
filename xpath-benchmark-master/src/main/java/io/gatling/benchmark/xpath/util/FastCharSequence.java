package io.gatling.benchmark.xpath.util;

public class FastCharSequence implements CharSequence {

	private final char[] chars;
	private final int offset;
	private final int count;
	
	public FastCharSequence(char[] chars) {
		this(chars, 0, chars.length);
	}
	
	public FastCharSequence(char[] chars, int offset, int count) {
		this.chars = chars;
		this.offset = offset;
		this.count = count;
	}

	@Override
    public int length() {
	    return count;
    }

	@Override
    public char charAt(int index) {
	    return chars[offset + index];
    }

	@Override
    public CharSequence subSequence(int start, int end) {
	    return new FastCharSequence(chars, offset + start, end - start);
    }
}
