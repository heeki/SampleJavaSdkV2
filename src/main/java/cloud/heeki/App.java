package cloud.heeki;

import static software.amazon.awssdk.transfer.s3.SizeConstant.MB;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.transfer.s3.S3ClientConfiguration;
import software.amazon.awssdk.transfer.s3.S3TransferManager;
import software.amazon.awssdk.transfer.s3.CompletedDownload;
import software.amazon.awssdk.transfer.s3.CompletedUpload;
import software.amazon.awssdk.transfer.s3.Download;
import software.amazon.awssdk.transfer.s3.DownloadRequest;
import software.amazon.awssdk.transfer.s3.Upload;
import software.amazon.awssdk.transfer.s3.UploadRequest;

public class App {
    public static void main(String[] args) {
        Region region = Region.US_EAST_1;
        S3ClientConfiguration config = S3ClientConfiguration.builder()
            .region(region)
            .maxConcurrency(10)
            .minimumPartSizeInBytes(1 * MB)
            .targetThroughputInGbps(0.5)
            .build();
        S3TransferManager tm = S3TransferManager.builder()
            .s3ClientConfiguration(config)
            .build();
        String file = "A001_C013_121586.mov";
        DownloadRequest dlr = DownloadRequest.builder()
            .destination(Paths.get("tmp/" + file))
            .getObjectRequest(b -> b.bucket("higs-assets").key("ffmpeg/" + file))
            .build();
        Download dl = tm.download(dlr);
        CompletedDownload dlc = dl.completionFuture().join();
        System.out.println(dlc.response());
    }
}
