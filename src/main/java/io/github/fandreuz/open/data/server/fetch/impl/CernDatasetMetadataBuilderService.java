package io.github.fandreuz.open.data.server.fetch.impl;

import io.github.fandreuz.open.data.server.fetch.FetchException;
import io.github.fandreuz.open.data.server.fetch.FileTypeNotRecognizedException;
import io.github.fandreuz.open.data.server.fetch.MetadataBuilderService;
import io.github.fandreuz.open.data.server.model.dataset.DatasetMetadata;
import io.github.fandreuz.open.data.server.model.dataset.DatasetType;
import jakarta.inject.Singleton;

import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.time.Instant;
import lombok.NonNull;

/**
 * Implementation of {@link MetadataBuilderService} for CERN Open data datasets.
 *
 * @author fandreuz
 */
@Singleton
final class CernDatasetMetadataBuilderService implements MetadataBuilderService<DatasetMetadata> {

   // Uniform Resource Name (URN)
   // schema:namespace:name
   private static final String UID_PATTERN = "cern-open-data:%s:%s";

   @Override
   public DatasetMetadata buildMetadata(@NonNull String collectionId, @NonNull Path file) {
      String fileName = file.toFile().getName();
      String uid = makeDatasetUid(collectionId, fileName);
      DatasetType type = findDatasetType(fileName);

      long sizeInBytes;
      try (FileChannel imageFileChannel = FileChannel.open(file)) {
         sizeInBytes = imageFileChannel.size();
      } catch (Exception exception) {
         throw new FetchException("An exception occurred while building metadata", exception);
      }
      return DatasetMetadata.builder() //
            .datasetId(uid) //
            .fileName(fileName) //
            .type(type) //
            .importTimestamp(Instant.now().toEpochMilli()) //
            .sizeInBytes(sizeInBytes) //
            .build();
   }

   // cern-open-data:19090:experimentData
   private static String makeDatasetUid(@NonNull String id, @NonNull String fileName) {
      return String.format(UID_PATTERN, id, fileNameWithoutExtension(fileName));
   }

   private static String fileNameWithoutExtension(@NonNull String fileName) {
      int lastDotIndex = fileName.lastIndexOf('.');
      return lastDotIndex > 0 ? fileName.substring(0, lastDotIndex) : fileName;
   }

   private static DatasetType findDatasetType(@NonNull String fileName) {
      String fileExtension = Utils.extractExtension(fileName);
      for (DatasetType datasetType : DatasetType.values()) {
         if (fileExtension.equals(datasetType.getExtension())) {
            return datasetType;
         }
      }
      throw new FileTypeNotRecognizedException(fileName);
   }
}
