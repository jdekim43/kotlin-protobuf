// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: google/protobuf/struct.proto

package com.google.protobuf;

public interface StructOrBuilder extends
    // @@protoc_insertion_point(interface_extends:google.protobuf.Struct)
    com.google.protobuf.MessageOrBuilder {

  /**
   * <pre>
   * Unordered map of dynamically typed values.
   * </pre>
   *
   * <code>map&lt;string, .google.protobuf.Value&gt; fields = 1;</code>
   */
  int getFieldsCount();
  /**
   * <pre>
   * Unordered map of dynamically typed values.
   * </pre>
   *
   * <code>map&lt;string, .google.protobuf.Value&gt; fields = 1;</code>
   */
  boolean containsFields(
      java.lang.String key);
  /**
   * Use {@link #getFieldsMap()} instead.
   */
  @java.lang.Deprecated
  java.util.Map<java.lang.String, com.google.protobuf.Value>
  getFields();
  /**
   * <pre>
   * Unordered map of dynamically typed values.
   * </pre>
   *
   * <code>map&lt;string, .google.protobuf.Value&gt; fields = 1;</code>
   */
  java.util.Map<java.lang.String, com.google.protobuf.Value>
  getFieldsMap();
  /**
   * <pre>
   * Unordered map of dynamically typed values.
   * </pre>
   *
   * <code>map&lt;string, .google.protobuf.Value&gt; fields = 1;</code>
   */
  /* nullable */
com.google.protobuf.Value getFieldsOrDefault(
      java.lang.String key,
      /* nullable */
com.google.protobuf.Value defaultValue);
  /**
   * <pre>
   * Unordered map of dynamically typed values.
   * </pre>
   *
   * <code>map&lt;string, .google.protobuf.Value&gt; fields = 1;</code>
   */
  com.google.protobuf.Value getFieldsOrThrow(
      java.lang.String key);
}
