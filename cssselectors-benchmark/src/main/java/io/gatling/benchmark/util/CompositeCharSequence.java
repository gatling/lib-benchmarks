package io.gatling.benchmark.util;

import java.util.Arrays;

public class CompositeCharSequence implements CharSequence {

  private CharSequence[] components;
  private int length;

  public CompositeCharSequence(CharSequence[] components) {
    this.components = components;
    this.length = Arrays.stream(components).mapToInt(CharSequence::length).sum();
  }

  public CharSequence[] getComponents() {
    return components;
  }

  @Override
  public int length() {
    return length;
  }

  @Override
  public char charAt(int index) {
    int off = 0;
    for (CharSequence component : components) {
      off += component.length();
      if (index < off) {
        return component.charAt(index - off);
      }
    }
    throw new IndexOutOfBoundsException();
  }

  @Override
  public CharSequence subSequence(int start, int end) {
    StringBuilder sub = new StringBuilder();
    // FIXME make faster, don't use charAt
    for (int idx = start; idx < end; ++idx) {
      sub.append(charAt(idx));
    }
    return sub;
  }
}

