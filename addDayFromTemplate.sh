#! /bin/zsh

# ./addDayFromTemplate.sh
YEAR=$1
DAY=$(printf "%02d" "$2")

KOTLIN_SRC_FOLDER="./src/main/kotlin/Year$YEAR/day$DAY"
FILE_PART1="$KOTLIN_SRC_FOLDER/Part1.kt"
FILE_PART2="$KOTLIN_SRC_FOLDER/Part2.kt"

if [ -f "$FILE_PART1" ]; then
    echo "$FILE_PART1 already exists."
    exit 1
fi

if [ -f "$FILE_PART2" ]; then
    echo "$FILE_PART2 already exists."
    exit 1
fi

PACKAGE_NAME="year$YEAR.day$DAY"

mkdir -p "$KOTLIN_SRC_FOLDER"
cp ./src/Part.kt.template "$FILE_PART1"
cp ./src/Part.kt.template "$FILE_PART2"

gsed -i "s/<PACKAGE_NAME>/$PACKAGE_NAME/g" "$FILE_PART1"
gsed -i "s/<PART_NUMBER>/1/g" "$FILE_PART1"
gsed -i "s/<PACKAGE_NAME>/$PACKAGE_NAME/g" "$FILE_PART2"
gsed -i "s/<PART_NUMBER>/2/g" "$FILE_PART2"

INPUTS_FOLDER_PATH="./src/main/resources/Year$YEAR/day$DAY/"

mkdir -p "$INPUTS_FOLDER_PATH"
touch "$INPUTS_FOLDER_PATH/inputs.txt"
touch "$INPUTS_FOLDER_PATH/test_inputs_part1.txt"
touch "$INPUTS_FOLDER_PATH/test_inputs_part2.txt"

curl "https://adventofcode.com/$YEAR/day/$2/input" --cookie "session=$(cat ./cookieSession.txt)" > "$INPUTS_FOLDER_PATH/inputs.txt"
curl -s "https://adventofcode.com/$YEAR/day/$2" --cookie "session=$(cat ./cookieSession.txt)"  | gsed -n '/<pre><code>/,/<\/code><\/pre>/p' | gsed -e 's/<pre><code>//g' -e 's/<\/code><\/pre>//g' > "$INPUTS_FOLDER_PATH/test_inputs_part1.txt"
cp "$INPUTS_FOLDER_PATH/test_inputs_part1.txt" "$INPUTS_FOLDER_PATH/test_inputs_part2.txt"