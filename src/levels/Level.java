package levels;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;

import resources.File;

public interface Level {
  String of();
  String fileName();
  default void writeFile(){
    Path p= File.startPath()
      .getParent()
      .resolve("assets")
      .resolve(fileName())
      .resolve(fileName()+".html");
    try {
      Files.createDirectories(p.getParent());
      Files.writeString(p,of());
    }
    catch (IOException e) { throw new UncheckedIOException(e); }
  }
}
