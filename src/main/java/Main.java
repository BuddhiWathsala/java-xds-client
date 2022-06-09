import com.google.protobuf.Descriptors;
import com.google.protobuf.RpcChannel;
import com.google.protobuf.StringValue;
import com.google.protobuf.Struct;
import com.google.protobuf.Value;
import io.envoyproxy.envoy.api.v2.ClusterDiscoveryServiceGrpc;
import io.envoyproxy.envoy.config.core.v3.BuildVersion;
import io.envoyproxy.envoy.config.core.v3.Locality;
import io.envoyproxy.envoy.config.core.v3.Node;
import io.envoyproxy.envoy.service.discovery.v3.AggregatedDiscoveryService;
import io.envoyproxy.envoy.service.discovery.v3.AggregatedDiscoveryServiceGrpc;
import io.envoyproxy.envoy.service.discovery.v3.DiscoveryRequest;
import io.envoyproxy.envoy.service.discovery.v3.DiscoveryResponse;
import io.envoyproxy.envoy.type.v3.SemanticVersion;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import io.grpc.xds.Bootstrapper;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class Main {

    public static void main(String[] args) throws InterruptedException {

        System.out.println();
        String target = "xds.domain.com:18000";
        ManagedChannel channel = ManagedChannelBuilder.forTarget(target).usePlaintext().build();

        try {
            AggregatedDiscoveryServiceGrpc.AggregatedDiscoveryServiceStub stub = AggregatedDiscoveryServiceGrpc.newStub(channel);
            AdsResponseStreamObserver adsResponseStreamObserver = new AdsResponseStreamObserver();
            StreamObserver<io.envoyproxy.envoy.service.discovery.v3.DiscoveryRequest> requestStreamObserver = stub.streamAggregatedResources(adsResponseStreamObserver);
            adsResponseStreamObserver.start(requestStreamObserver);
            Thread.sleep(10000);

        } finally {


        }
    }
}
