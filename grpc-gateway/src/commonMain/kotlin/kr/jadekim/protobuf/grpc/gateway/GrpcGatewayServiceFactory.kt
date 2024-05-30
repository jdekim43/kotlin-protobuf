package kr.jadekim.protobuf.grpc.gateway

import kr.jadekim.protobuf.type.ProtobufService
import kr.jadekim.protobuf.type.ProtobufServiceFactory

interface GrpcGatewayServiceFactory<I : ProtobufService, C : I> : ProtobufServiceFactory<I, C, GrpcGatewayClientOption>
