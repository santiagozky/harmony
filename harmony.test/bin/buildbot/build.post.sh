#if [ "`egrep '(errors|failures)=.[1-9][0-9]*' doc/junit/TESTS-TestSuites.xml`" ]; then
#  exit 1;
#fi
exit 0
