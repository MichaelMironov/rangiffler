package tests.grpc;

import config.Config;
import guru.qa.grpc.rangiffler.grpc.RangifflerCountriesServiceGrpc;
import io.grpc.Channel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.Metadata;
import io.qameta.allure.grpc.AllureGrpc;
import jupiter.annotation.meta.GrpcTest;

@GrpcTest
public abstract class BaseGrpcTest {

    protected static final Config CFG = Config.getConfig();

    private static final Channel channel;

    static {
        Metadata metadata = new Metadata();
        metadata.put(Metadata.Key.of("Authorization", Metadata.ASCII_STRING_MARSHALLER), "token");

        channel = ManagedChannelBuilder
                .forAddress(CFG.geoGrpcUrl(), CFG.geoGrpcPort())
                .intercept(new AllureGrpc())
                .usePlaintext()
                .build();
    }

    RangifflerCountriesServiceGrpc.RangifflerCountriesServiceBlockingStub serviceBlockingStub =
            RangifflerCountriesServiceGrpc.newBlockingStub(channel);
}
