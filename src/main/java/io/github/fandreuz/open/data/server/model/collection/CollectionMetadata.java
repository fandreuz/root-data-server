package io.github.fandreuz.open.data.server.model.collection;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

import java.util.Comparator;

/**
 * Collection metadata.
 *
 * @author fandreuz
 */
@Getter
@Builder
@ToString
@EqualsAndHashCode
@AllArgsConstructor
public class CollectionMetadata implements Comparable<CollectionMetadata> {

   private static final Comparator<CollectionMetadata> COMPARATOR = Comparator.comparing(CollectionMetadata::getId);

   @NonNull
   private String id;

   @NonNull
   private String name;
   @NonNull
   private String shortDescription;
   @NonNull
   private String longDescription;
   @NonNull
   private Integer year;
   @Nonnull
   private String experimentName;
   // Collision, derived or simulated. Didn't want to provide an enum to make it
   // more flexible.
   @Nullable
   private Long eventsCount;
   @Nonnull
   private String type;
   @NonNull
   private String keyword;
   @NonNull
   private String tag;
   @NonNull
   private String citeText;
   @NonNull
   private String doi;
   @NonNull
   private String license;

   public CollectionMetadata() {
      // Required by the serialization layer
   }

   @Override
   public int compareTo(@NonNull CollectionMetadata dataset) {
      return COMPARATOR.compare(this, dataset);
   }
}