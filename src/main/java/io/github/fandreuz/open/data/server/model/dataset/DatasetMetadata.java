package io.github.fandreuz.open.data.server.model.dataset;

import java.util.Comparator;

import io.github.fandreuz.open.data.server.model.collection.CollectionMetadata;
import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

/**
 * Dataset metadata.
 *
 * @author fandreuz
 */
@Getter
@Builder
@ToString
@EqualsAndHashCode
@AllArgsConstructor
public class DatasetMetadata implements Comparable<DatasetMetadata> {

   private static final Comparator<DatasetMetadata> COMPARATOR = Comparator.nullsLast( //
         Comparator.comparing( //
               DatasetMetadata::getDatasetId, //
               Comparator.nullsLast(Comparator.naturalOrder()) //
         ) //
   );

   @NonNull
   private String datasetId;

   @NonNull
   private String fileName;
   @NonNull
   private DatasetType type;
   private long sizeInBytes;

   // These are set when we have a CSV file.
   @Nullable
   private Long numberOfColumns;
   @Nullable
   private String commaSeparatedColumnNames;

   private long importTimestamp;

   @Nullable
   private CollectionMetadata collectionMetadata;

   /**
    * Construct a metadata instance from a database object.
    *
    * @param datasetMetadataDO
    *            database object.
    * @return a dataset instance.
    */
   public static DatasetMetadata fromDatabaseObject(DatasetMetadataDO datasetMetadataDO) {
      return DatasetMetadata.builder() //
            .datasetId(datasetMetadataDO.getDatasetId()) //
            .fileName(datasetMetadataDO.getFileName()) //
            .type(datasetMetadataDO.getType()) //
            .sizeInBytes(datasetMetadataDO.getSizeInBytes()) //
            .numberOfColumns(datasetMetadataDO.getNumberOfColumns()) //
            .commaSeparatedColumnNames(datasetMetadataDO.getCommaSeparatedColumnNames()) //
            .importTimestamp(datasetMetadataDO.getImportTimestamp()) //
            .build();
   }

   /**
    * Attach the given collection metadata to the provided dataset metadata.
    *
    * @param datasetMetadata
    *            source dataset metadata.
    * @param collectionMetadata
    *            collection metadata to be attached.
    * @return a shallow copy of the given dataset metadata.
    */
   public static DatasetMetadata attachCollectionMetadata(DatasetMetadata datasetMetadata,
         CollectionMetadata collectionMetadata) {
      return DatasetMetadata.builder() //
            .datasetId(datasetMetadata.getDatasetId()) //
            .fileName(datasetMetadata.getFileName()) //
            .type(datasetMetadata.getType()) //
            .sizeInBytes(datasetMetadata.getSizeInBytes()) //
            .numberOfColumns(datasetMetadata.getNumberOfColumns()) //
            .commaSeparatedColumnNames(datasetMetadata.getCommaSeparatedColumnNames()) //
            .importTimestamp(datasetMetadata.getImportTimestamp()) //
            .collectionMetadata(collectionMetadata) //
            .build();
   }

   /**
    * Attach the given CSV metadata to the provided dataset metadata.
    *
    * @param datasetMetadata
    *            source dataset metadata.
    * @param numberOfColumns
    *            number of columns in the dataset.
    * @param commaSeparatedColumnNames
    *            columns of the dataset.
    * @return a shallow copy of the given dataset metadata.
    */
   public static DatasetMetadata attachCsvMetadata(DatasetMetadata datasetMetadata, long numberOfColumns,
         @NonNull String commaSeparatedColumnNames) {
      return DatasetMetadata.builder() //
            .datasetId(datasetMetadata.getDatasetId()) //
            .fileName(datasetMetadata.getFileName()) //
            .type(datasetMetadata.getType()) //
            .sizeInBytes(datasetMetadata.getSizeInBytes()) //
            .numberOfColumns(numberOfColumns) //
            .commaSeparatedColumnNames(commaSeparatedColumnNames) //
            .importTimestamp(datasetMetadata.getImportTimestamp()) //
            .collectionMetadata(datasetMetadata.getCollectionMetadata()) //
            .build();
   }

   /**
    * Transform this instance in a database object.
    *
    * @return a database object.
    */
   public DatasetMetadataDO asDatabaseObject() {
      return DatasetMetadataDO.builder() //
            .datasetId(getDatasetId()) //
            .fileName(getFileName()) //
            .type(getType()) //
            .sizeInBytes(getSizeInBytes()) //
            .numberOfColumns(getNumberOfColumns()) //
            .commaSeparatedColumnNames(getCommaSeparatedColumnNames()) //
            .importTimestamp(getImportTimestamp()) //
            .build();
   }

   @Override
   public int compareTo(@NonNull DatasetMetadata dataset) {
      return COMPARATOR.compare(this, dataset);
   }
}
