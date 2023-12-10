#! /bin/zsh

ALL_PARTS_KT_FILE="src/main/kotlin/AllParts.kt"

printf "val allParts = listOf(\n" > $ALL_PARTS_KT_FILE
ls -l  src/main/kotlin/year*/da*/Part*  | sed 's|.*src/main/kotlin/\(year[0-9]\{4\}\)/\(day[0-3][0-9]\)/Part\([12]\).kt|    \1.\2.part\3,|g' >> $ALL_PARTS_KT_FILE
printf ")" >> $ALL_PARTS_KT_FILE
