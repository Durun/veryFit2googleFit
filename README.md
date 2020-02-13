# vFit2gFit(under construction)
A tool to parse log files of [VeryFitPro](https://play.google.com/store/apps/details?id=com.veryfit2hr.second)

## Build
```shell
# build
./gradlew shadowJar
```

## Usage
```shell
# parse
java -jar build/libs/veryFit2googleFit-0.1-SNAPSHOT-all.jar /path/to/VeryFitPro db.db

# optional
sqlite3 db.db < build/resources/main/dateView.sql
```
and open `db.db`