# Connectaimes 

I made this game as a birthday present for a friend, in 2017. The repo originally contained his name and face, and the mp3 "Showdown" by Creo. Those things had to be removed before I could publish this. And so the original git files had to be deleted, or the PII and copyrighted material could have been retrieved from the git history.

The intro sequence is timed to that song, "Showdown". I recommend downloading it and replacing the empty version of the mp3 file by the same name in this repo.

Compile with:

```
./gradlew clean desktop:dist
```

Run with:

```
java -jar connectaimes-1.0.0.jar
```