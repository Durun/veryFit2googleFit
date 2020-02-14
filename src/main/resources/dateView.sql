CREATE VIEW heartRate_date AS
SELECT 
	datetime("1970-01-01", "+"||(timeAt/1000)||" seconds") AS datetime,
	date("1970-01-01", "+"||(timeAt/1000)||" seconds") AS date,
	time("1970-01-01", "+"||(timeAt/1000)||" seconds") AS time,
	bpm
FROM heartRate;

CREATE VIEW sleep_date AS
SELECT 
	datetime("1970-01-01", "+"||(timeFrom/1000)||" seconds") AS datetimeFrom,
	date("1970-01-01", "+"||(timeFrom/1000)||" seconds") AS dateFrom,
	time("1970-01-01", "+"||(timeFrom/1000)||" seconds") AS timeFrom,
	datetime("1970-01-01", "+"||(timeTo/1000)||" seconds") AS datetimeTo,
	date("1970-01-01", "+"||(timeTo/1000)||" seconds") AS dateTo,
	time("1970-01-01", "+"||(timeTo/1000)||" seconds") AS timeTo,
	(timeTo - timeFrom) / 1000 / 60 AS minutes
FROM sleep;

CREATE VIEW sleepDetail_date AS
SELECT 
	datetime("1970-01-01", "+"||(timeFrom/1000)||" seconds") AS datetimeFrom,
	date("1970-01-01", "+"||(timeFrom/1000)||" seconds") AS dateFrom,
	time("1970-01-01", "+"||(timeFrom/1000)||" seconds") AS timeFrom,
	datetime("1970-01-01", "+"||(timeTo/1000)||" seconds") AS datetimeTo,
	date("1970-01-01", "+"||(timeTo/1000)||" seconds") AS dateTo,
	time("1970-01-01", "+"||(timeTo/1000)||" seconds") AS timeTo,
	(timeTo - timeFrom) / 1000 / 60 AS minutes,
	depth
FROM sleepDetail;
