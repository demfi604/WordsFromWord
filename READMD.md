# WordsFromWord

## Overview / Description
**WordsFromWord — Level Generator** is a Java 8 console application designed to automatically generate game levels for the word puzzle *"Words from Word"* (also known as *"Anagrams"* or *"Wordscapes"*).

The program reads a word dictionary, a list of target main words, and difficulty rules from JSON files, filters valid sub-words based on strict letter frequency validation, and exports structured game levels to `output/levels.json`.
<!-- Краткое описание проекта: что это за программа и для какой игры/задачи она создана -->

## Key Features
- **Automated Level Creation:** The program automatically selects all relevant sub-words from the dictionary for each given main word, eliminating the need to manually construct levels.
- **Strict Letter Frequency Validation:** Ensures that each letter in a sub-word does not appear more times than it exists in the main word.
- **Data Cleaning & Normalization:** Removes extra spaces, converts all words to uppercase (respecting Russian locale settings and preserving the letter 'Ё'), and removes duplicates.
- **Rule-Based Filtering:** Excludes sub-words shorter than `minSubWordLength`, excludes the main word itself, and skips levels that do not meet `minWordsToPass`.
- **Dynamic Difficulty Assignment:** Automatically classifies each generated level as `EASY`, `MEDIUM`, or `HARD` based on configurable word length and sub-word count thresholds.
- **JSON Serialization:** Reads input data from JSON files and exports clean, formatted level data into `output/levels.json`.

<!-- Список основных возможностей и правил работы программы -->

## Tech Stack & Requirements
To build and run this project, you need the following tools and libraries:
- **Java Development Kit (JDK):** Java 8 (version 1.8)
- **Build Tool:** Apache Maven (or the included Maven Wrapper `./mvnw`)
- **IDE:** IntelliJ IDEA (recommended)
- **Libraries & Dependencies:**
    - `com.google.code.gson:gson:2.10.1` — for JSON parsing and serialization
    - `junit:junit:4.13.2` — for unit testing
<!-- Необходимые инструменты: версия Java, система сборки (Maven), библиотеки и IDE -->

## Project Structure
<!-- Дерево папок и описание назначения каждого пакета/файла -->

## Installation & How to Run
<!-- Пошаговая инструкция: как скачать, собрать и запустить программу и тесты -->

## Input & Output Examples
<!-- Примеры входных данных (JSON) и результата работы программы -->

## Screenshots
<!-- Скриншоты запуска программы в консоли и прохождения тестов -->

## Known Limitations
<!-- Слабые места и ограничения алгоритма (например, буква Ё/Е, зависимость от словаря) -->

## Authors / Team
<!-- Имена и роли участников команды -->