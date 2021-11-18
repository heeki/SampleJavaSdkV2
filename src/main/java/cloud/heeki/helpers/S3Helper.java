package cloud.heeki.helpers;

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

import static software.amazon.awssdk.transfer.s3.SizeConstant.MB;

public class S3Helper {
    private final Region region = Region.US_EAST_1;
    private final S3ClientConfiguration config;
    private final S3TransferManager tm;
    private final String s3_bucket;
    private final String s3_prefix;

    public S3Helper(String bucket, String prefix) {
        this.s3_bucket = bucket;
        this.s3_prefix = prefix;
        this.config = S3ClientConfiguration.builder()
            .region(region)
            .maxConcurrency(10)
            .minimumPartSizeInBytes(1 * MB)
            .targetThroughputInGbps(0.5)
            .build();
        this.tm = S3TransferManager.builder()
            .s3ClientConfiguration(config)
            .build();
    }

    public void executeS3Transfer(String file) {
        DownloadRequest dlr = DownloadRequest.builder()
            .destination(Paths.get("tmp/" + file))
            .getObjectRequest(b -> b.bucket(this.s3_bucket).key(this.s3_prefix + file))
            .build();
        Download dl = tm.download(dlr);
        CompletedDownload dlc = dl.completionFuture().join();
        System.out.println(dlc.response());
    }
}