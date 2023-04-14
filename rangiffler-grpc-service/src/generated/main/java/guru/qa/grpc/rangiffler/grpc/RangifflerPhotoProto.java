// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: rangiffler-photo.proto

package guru.qa.grpc.rangiffler.grpc;

public final class RangifflerPhotoProto {
  private RangifflerPhotoProto() {}
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistryLite registry) {
  }

  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistry registry) {
    registerAllExtensions(
        (com.google.protobuf.ExtensionRegistryLite) registry);
  }
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_guru_qa_Photo_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_guru_qa_Photo_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_guru_qa_Photos_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_guru_qa_Photos_fieldAccessorTable;

  public static com.google.protobuf.Descriptors.FileDescriptor
      getDescriptor() {
    return descriptor;
  }
  private static  com.google.protobuf.Descriptors.FileDescriptor
      descriptor;
  static {
    java.lang.String[] descriptorData = {
      "\n\026rangiffler-photo.proto\022\007guru.qa\032\033googl" +
      "e/protobuf/empty.proto\032\032rangiffler-count" +
      "ries.proto\"l\n\005Photo\022\n\n\002id\030\001 \001(\t\022!\n\007count" +
      "ry\030\002 \001(\0132\020.guru.qa.Country\022\r\n\005photo\030\003 \001(" +
      "\t\022\023\n\013description\030\004 \001(\t\022\020\n\010username\030\005 \001(\t" +
      "\"(\n\006Photos\022\036\n\006photos\030\001 \003(\0132\016.guru.qa.Pho" +
      "to2\256\002\n\026RangifflerPhotoService\022=\n\020getPhot" +
      "osForUser\022\026.google.protobuf.Empty\032\017.guru" +
      ".qa.Photos\"\000\022?\n\022getPhotosForFriend\022\026.goo" +
      "gle.protobuf.Empty\032\017.guru.qa.Photos\"\000\022,\n" +
      "\010addPhoto\022\016.guru.qa.Photo\032\016.guru.qa.Phot" +
      "o\"\000\022-\n\teditPhoto\022\016.guru.qa.Photo\032\016.guru." +
      "qa.Photo\"\000\0227\n\013deletePhoto\022\016.guru.qa.Phot" +
      "o\032\026.google.protobuf.Empty\"\000B6\n\034guru.qa.g" +
      "rpc.rangiffler.grpcB\024RangifflerPhotoProt" +
      "oP\001b\006proto3"
    };
    descriptor = com.google.protobuf.Descriptors.FileDescriptor
      .internalBuildGeneratedFileFrom(descriptorData,
        new com.google.protobuf.Descriptors.FileDescriptor[] {
          com.google.protobuf.EmptyProto.getDescriptor(),
          guru.qa.grpc.rangiffler.grpc.RangifflerCountriesProto.getDescriptor(),
        });
    internal_static_guru_qa_Photo_descriptor =
      getDescriptor().getMessageTypes().get(0);
    internal_static_guru_qa_Photo_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_guru_qa_Photo_descriptor,
        new java.lang.String[] { "Id", "Country", "Photo", "Description", "Username", });
    internal_static_guru_qa_Photos_descriptor =
      getDescriptor().getMessageTypes().get(1);
    internal_static_guru_qa_Photos_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_guru_qa_Photos_descriptor,
        new java.lang.String[] { "Photos", });
    com.google.protobuf.EmptyProto.getDescriptor();
    guru.qa.grpc.rangiffler.grpc.RangifflerCountriesProto.getDescriptor();
  }

  // @@protoc_insertion_point(outer_class_scope)
}
