package guru.qa.service;

import com.google.protobuf.Empty;
import guru.qa.data.CountryEntity;
import guru.qa.data.repository.CountryRepository;
import guru.qa.grpc.rangiffler.grpc.Countries;
import guru.qa.grpc.rangiffler.grpc.Country;
import guru.qa.grpc.rangiffler.grpc.RangifflerCountriesServiceGrpc;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import net.devh.boot.grpc.server.service.GrpcService;

import java.util.List;

@GrpcService
@RequiredArgsConstructor
public class GrpcCountriesService extends RangifflerCountriesServiceGrpc.RangifflerCountriesServiceImplBase {

    private final CountryRepository countryRepository;

    @SneakyThrows
    @Override
    public void getCountries(Empty request, StreamObserver<Countries> responseObserver) {

        List<CountryEntity> countries = countryRepository.findAll();

        Countries response = Countries.newBuilder()
                .addAllAllCountries(countries.stream().map(country ->
                                Country.newBuilder()
                                        .setUuid(country.getId().toString())
                                        .setCode(country.getCode())
                                        .setName(country.getName())
                                        .build())
                        .toList())
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
