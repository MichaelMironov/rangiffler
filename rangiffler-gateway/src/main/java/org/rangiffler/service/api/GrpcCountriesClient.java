package org.rangiffler.service.api;

import com.google.protobuf.Empty;
import guru.qa.grpc.rangiffler.grpc.RangifflerCountriesServiceGrpc;
import io.grpc.StatusRuntimeException;
import jakarta.annotation.Nonnull;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.rangiffler.model.CountryJson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class GrpcCountriesClient {

    private static final Logger LOG = LoggerFactory.getLogger(GrpcCountriesClient.class);
    private static final Empty EMPTY = Empty.getDefaultInstance();

    @GrpcClient("grpcCountriesClient")
    private RangifflerCountriesServiceGrpc.RangifflerCountriesServiceBlockingStub rangifflerCountriesServiceStub;

    public @Nonnull
    List<CountryJson> getCountries() {

        try {
            return rangifflerCountriesServiceStub.getCountries(EMPTY).getAllCountriesList()
                    .stream().map(CountryJson::fromGrpcMessage)
                    .collect(Collectors.toList());
        } catch (StatusRuntimeException e) {
            LOG.error("### Error while calling gRPC server ", e);
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "The gRPC operation was cancelled", e);
        }
    }
}
