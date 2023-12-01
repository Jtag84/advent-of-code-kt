#! /bin/zsh

# ./addDayFromTemplate.sh
YEAR=$1
DAY=$(printf "%02d" $2)

FILE="./src/Year$YEAR/Day$DAY.kt"

if [ -f "$FILE" ]; then
    echo "$FILE already exists."
    exit 1
fi

PACKAGE_NAME="year$YEAR"

cp ./src/Day.kt.template $FILE

gsed -i "s/PACKAGE_NAME/$PACKAGE_NAME/" "$FILE"
gsed -i "s/{YEAR}/$YEAR/" "$FILE"
gsed -i "s/{DAY}/$2/" "$FILE"

touch "./src/Year$YEAR/Day$DAY.txt"
touch "./src/Year$YEAR/Day${DAY}_test.txt"

curl https://adventofcode.com/$YEAR/day/$2/input --cookie "session=`cat ./cookieSession.txt`" > "./src/Year$YEAR/Day$DAY.txt"
curl -s https://adventofcode.com/$YEAR/day/$2 --cookie "session=`cat ./cookieSession.txt`"  | gsed -n '/<pre><code>/,/<\/code><\/pre>/p' | gsed -e 's/<pre><code>//g' -e 's/<\/code><\/pre>//g' > "./src/Year$YEAR/Day${DAY}_test.txt"