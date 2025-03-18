package mappings;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.common.JsonException;
import com.github.tomakehurst.wiremock.standalone.MappingsLoader;
import com.github.tomakehurst.wiremock.stubbing.StubMappings;
import helper.ResourceReader;
import lombok.SneakyThrows;

import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.equalToJson;
import static com.github.tomakehurst.wiremock.client.WireMock.jsonResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.okJson;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;

public class WeatherApiMappingLoader implements MappingsLoader {

    private final ResourceReader reader = ResourceReader.builder()
            .basePath("stubs/weather/api/")
            .build();

    @Override
    @SneakyThrows(JsonException.class)
    public void loadMappingsInto(StubMappings stubMappings) {
        stubMappings.addMapping(
                WireMock.get(urlPathEqualTo("/v1/current.json"))
                        .withQueryParam("q", equalTo("Brasilia"))
                        .withQueryParam("key", equalTo("2ee10f"))
                        .willReturn(okJson(reader.readJson("positive-brasilia.json")))
                        .build()
        );

        stubMappings.addMapping(
                WireMock.get(urlPathEqualTo("/v1/current.json"))
                        .withQueryParam("q", equalTo("Cairo"))
                        .withQueryParam("key", equalTo("960aa9"))
                        .willReturn(okJson(reader.readJson("positive-cairo.json")))
                        .build()
        );

        stubMappings.addMapping(
                WireMock.get(urlPathEqualTo("/v1/current.json"))
                        .withQueryParam("q", equalTo("Moscow"))
                        .withQueryParam("key", equalTo("c6ab20"))
                        .willReturn(okJson(reader.readJson("positive-moscow.json")))
                        .build()
        );

        stubMappings.addMapping(
                WireMock.get(urlPathEqualTo("/v1/current.json"))
                        .withQueryParam("q", equalTo("Paris"))
                        .withQueryParam("key", equalTo("9f2d78"))
                        .willReturn(okJson(reader.readJson("positive-paris.json")))
                        .build()
        );

        stubMappings.addMapping(
                WireMock.get(urlPathEqualTo("/v1/current.json"))
                        .withQueryParam("q", equalTo("Tokyo"))
                        .withQueryParam("key", equalTo("f21f21"))
                        .willReturn(okJson(reader.readJson("positive-tokyo.json")))
                        .build()
        );

        stubMappings.addMapping(
                WireMock.get(urlPathEqualTo("/v1/current.json"))
                        .withQueryParam("q", equalTo("Samara"))
                        .willReturn(
                                jsonResponse(
                                        reader.readJson("negative-parameter-key-is-not-provided.json"),
                                        401
                                )
                        )
                        .build()
        );

        stubMappings.addMapping(
                WireMock.get(urlPathEqualTo("/v1/current.json"))
                        .withQueryParam("key", equalTo("b41b7f"))
                        .willReturn(
                                jsonResponse(
                                        reader.readJson("negative-parameter-q-is-not-provided.json"),
                                        400
                                )
                        )
                        .build()
        );

        stubMappings.addMapping(
                WireMock.get(urlPathEqualTo("/v1/current.json"))
                        .withQueryParam("q", equalTo("Narnia123"))
                        .withQueryParam("key", equalTo("77a701"))
                        .willReturn(
                                jsonResponse(
                                        reader.readJson("negative-no-location-found-matching-q.json"),
                                        400
                                )
                        )
                        .build()
        );

        stubMappings.addMapping(
                WireMock.post(urlPathEqualTo("/v1/current.json"))
                        .withQueryParam("q", equalTo("bulk"))
                        .withQueryParam("key", equalTo("03df24"))
//                        .withRequestBody(WireMock.equalTo("""
//                          {
//                            "locations": [
//                                {
//                                    "q":
//                                }
//                            ]
//                          }
//                        """))
                        .withHeader("Content-type", equalTo("application/json"))
                        .willReturn(
                                jsonResponse(
                                        reader.readJson("negative-body-in-bulk-request-is-invalid.json"),
                                400
                                )
                        )
                        .build()
        );
    }


}
