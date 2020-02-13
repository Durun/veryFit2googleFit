CREATE VIEW heartRateDate AS
SELECT 
	datetime("1970-01-01", "+"||(timeAt/1000)||" seconds") AS date,
	bpm
FROM heartRate;
