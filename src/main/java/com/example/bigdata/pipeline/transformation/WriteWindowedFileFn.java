/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.bigdata.pipeline.transformation;

import lombok.extern.slf4j.Slf4j;
import org.apache.beam.sdk.io.FileBasedSink;
import org.apache.beam.sdk.io.FileBasedSink.FilenamePolicy;
import org.apache.beam.sdk.io.FileBasedSink.OutputFileHints;
import org.apache.beam.sdk.io.TextIO;
import org.apache.beam.sdk.io.fs.ResolveOptions.StandardResolveOptions;
import org.apache.beam.sdk.io.fs.ResourceId;
import org.apache.beam.sdk.transforms.DoFn;
import org.apache.beam.sdk.transforms.PTransform;
import org.apache.beam.sdk.transforms.windowing.BoundedWindow;
import org.apache.beam.sdk.transforms.windowing.IntervalWindow;
import org.apache.beam.sdk.transforms.windowing.PaneInfo;
import org.apache.beam.sdk.values.PCollection;
import org.apache.beam.sdk.values.PDone;

import javax.annotation.Nullable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;


/**
 * A {@link DoFn} that writes elements to files with names deterministically derived from the lower
 * and upper bounds of their key (an {@link IntervalWindow}).
 *
 * <p>This is test utility code, not for end-users, so examples can be focused on their primary
 * lessons.
 */
@Slf4j
public class WriteWindowedFileFn extends PTransform<PCollection<String>, PDone> {
  private static final DateFormat FORMATTER = new SimpleDateFormat("yyyy-mm-dd' T 'HH-mm");;

    private String filenamePrefix;
  @Nullable private Integer numShards;

  /**
   * Instantiates a new Write windowed file fn.
   *
   * @param filenamePrefix the filename prefix
   * @param numShards the num shards
   */
public WriteWindowedFileFn(String filenamePrefix, Integer numShards) {
    this.filenamePrefix = filenamePrefix;
    this.numShards = numShards;
  }

  @Override
  public PDone expand(PCollection<String> input) {
    ResourceId resource = FileBasedSink.convertToFileResourceIfPossible(filenamePrefix);

    TextIO.Write write =
            TextIO.write()
                    .to(new PerWindowFiles(resource))
                    .withTempDirectory(resource.getCurrentDirectory())
                    .withWindowedWrites();
      if (numShards != null) {
      write = write.withNumShards(numShards);
    }
    return input.apply(write);
  }

  /**
   * A {@link FilenamePolicy} produces a base file name for a write based on metadata about the data
   * being written. This always includes the shard number and the total number of shards. For
   * windowed writes, it also includes the window and pane index (a sequence number assigned to each
   * trigger firing).
   */
public static class PerWindowFiles extends FilenamePolicy {

    private final ResourceId baseFilename;

    /**
     * Instantiates a new Per window files.
     *
     * @param baseFilename the base filename
     */
public PerWindowFiles(ResourceId baseFilename) {
      this.baseFilename = baseFilename;
    }

    /**
     * Filename prefix for window string.
     *
     * @param window the window
     * @return the string
     */
public String filenamePrefixForWindow(IntervalWindow window) {
      String prefix =
          baseFilename.isDirectory() ? "" : firstNonNull(baseFilename.getFilename(), "");
      return String.format(
          "%s-%s-%s", prefix, FORMATTER.format(window.start().toDate()), FORMATTER.format(window.end().toDate()));
    }

    @Override
    public ResourceId windowedFilename(
        int shardNumber,
        int numShards,
        BoundedWindow window,
        PaneInfo paneInfo,
        OutputFileHints outputFileHints) {
      IntervalWindow intervalWindow = (IntervalWindow) window;
      String filename =
          String.format(
              "%s_%s-of-%s%s",
              filenamePrefixForWindow(intervalWindow),
              shardNumber,
              numShards,
              outputFileHints.getSuggestedFilenameSuffix());
      return baseFilename
          .getCurrentDirectory()
          .resolve(filename, StandardResolveOptions.RESOLVE_FILE);
    }

    @Override
    public ResourceId unwindowedFilename(
        int shardNumber, int numShards, OutputFileHints outputFileHints) {
      throw new UnsupportedOperationException("Unsupported.");
    }

    /**
     * First non null t.
     *
     * @param <T>   the type parameter
     * @param first the first
     * @param second the second
     * @return the t
     */
public static <T> T firstNonNull(@Nullable T first, @Nullable T second) {
      if (first != null) {
        return first;
      }
      if (second != null) {
        return second;
      }
      throw new NullPointerException("Both parameters are null");
    }
  }
}
