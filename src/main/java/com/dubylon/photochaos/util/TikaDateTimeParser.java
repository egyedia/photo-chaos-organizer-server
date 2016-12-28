package com.dubylon.photochaos.util;

import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.mp4.MP4Parser;
import org.apache.tika.sax.BodyContentHandler;

import java.io.FileInputStream;
import java.nio.file.Path;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.*;

public final class TikaDateTimeParser {

  private final static List<String> creationMetaCandidateNames;
  private final static DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_INSTANT;

  static {
    creationMetaCandidateNames = new ArrayList<>();
    creationMetaCandidateNames.add("meta:creation-date");
    creationMetaCandidateNames.add("Creation-Date");
    creationMetaCandidateNames.add("dcterms:created");

    creationMetaCandidateNames.add("date");

    creationMetaCandidateNames.add("meta:save-date");
    creationMetaCandidateNames.add("Last-Save-Date");

    creationMetaCandidateNames.add("dcterms:modified");
    creationMetaCandidateNames.add("Last-Modified");
    creationMetaCandidateNames.add("modified");
  }

  private TikaDateTimeParser() {
  }

  public static Pair<Instant, Exception> parse(Path path) {
    Instant instant = null;
    try {
      BodyContentHandler handler = new BodyContentHandler();
      Metadata metadata = new Metadata();
      FileInputStream is = new FileInputStream(path.toFile());
      ParseContext pctx = new ParseContext();

      MP4Parser MP4Parser = new MP4Parser();
      MP4Parser.parse(is, handler, metadata, pctx);
      String[] metadataNames = metadata.names();
      Set<String> metadataNamesFound = new HashSet<>(Arrays.asList(metadataNames));

      for (String candidate : creationMetaCandidateNames) {
        if (metadataNamesFound.contains(candidate)) {
          try {
            TemporalAccessor ta = dateTimeFormatter.parse(metadata.get(candidate));
            instant = Instant.from(ta);
            if (instant != null) {
              ZonedDateTime zonedDateTime = instant.atZone(ZoneId.systemDefault());
              LocalDateTime localDateTime = zonedDateTime.toLocalDateTime();
              instant = localDateTime.toInstant(ZoneOffset.UTC);
            }
          } catch (Exception e) {
            // do nothing
          }
          if (instant != null) {
            return new Pair(instant, null);
          }
        }
      }
      return new Pair(instant, null);
    } catch (Exception e) {
      return new Pair(null, e);
    }
  }
}
