#!/bin/bash


# This shell script is intended for testing purpose
INSIGHT_ROOT=$(dirname ${BASH_SOURCE})

PROJECT_PATH=${GRADER_ROOT}/..


# setup testing output folder
function setup_testing_input_output {
  TEST_OUTPUT_PATH=${GRADER_ROOT}/temp
  if [ -d ${TEST_OUTPUT_PATH} ]; then
    rm -rf ${TEST_OUTPUT_PATH}
  fi

  mkdir -p ${TEST_OUTPUT_PATH}

  cp -r ${PROJECT_PATH}/src ${TEST_OUTPUT_PATH}
  cp -r ${PROJECT_PATH}/run.sh ${TEST_OUTPUT_PATH}
  cp -r ${PROJECT_PATH}/input ${TEST_OUTPUT_PATH}
  cp -r ${PROJECT_PATH}/output ${TEST_OUTPUT_PATH}

  rm -r ${TEST_OUTPUT_PATH}/input/*
  rm -r ${TEST_OUTPUT_PATH}/output/*
  cp -r ${INSIGHT_ROOT}/tests/${test_folder}/input/complaints.csv ${TEST_OUTPUT_PATH}/input/complaints.csv
}

function compare_outputs {
  OUTPUT_FILENAME=report.csv
  PROJECT_ANSWER_PATH=${INSIGHT_ROOT}/temp/output/${OUTPUT_FILENAME}
  TEST_ANSWER_PATH=${INSIGHT_ROOT}/tests/${test_folder}/output/${OUTPUT_FILENAME}

  DIFF_RESULT=$(diff -bB ${PROJECT_ANSWER_PATH} ${TEST_ANSWER_PATH} | wc -l)
  if [ "${DIFF_RESULT}" -eq "0" ] && [ -f ${PROJECT_ANSWER_PATH} ]; then
    echo -e "[PASS]: ${test_folder} ${OUTPUT_FILENAME}"
    PASS_CNT=$(($PASS_CNT+1))
  else
    echo -e "[FAIL$]: ${test_folder}"
    diff ${PROJECT_ANSWER_PATH} ${TEST_ANSWER_PATH}
  fi
}

function run_tests {
  TEST_FOLDERS=$(ls ${INSIGHT_ROOT}/tests)
  TOTAL_TESTS=$(($(echo $(echo ${TEST_FOLDERS} | wc -w))))
  PASS_CNT=0

  # Loop through all tests
  for test_folder in ${TEST_FOLDERS}; do

    setup_testing_input_output

    cd ${INSIGHT_ROOT}/temp
    bash run.sh 2>&1
    cd ../

    compare_outputs
  done

  echo "${PASS_CNT} of ${TOTAL_TESTS} tests passed"
  echo "${PASS_CNT} of ${TOTAL_TESTS} tests passed" >> ${INSIGHT_ROOT}/passed_test_report.txt
}


# run tests
run_tests

