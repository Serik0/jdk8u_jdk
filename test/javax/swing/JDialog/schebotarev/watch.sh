while true
do
	LANG=C date
	ps -Af|grep java |grep -v compile-server |grep -v timeslottr
	sleep 1
done
