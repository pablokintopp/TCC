	@ECHO off
	setlocal EnableDelayedExpansion
	copy ..\Preprocessamento\bin\Preprocessamento.class Preprocessamento.class
	set FILENAME=2004_original.csv		
		set OUTPUT=_2004.csv
		java Preprocessamento --remove ; 5 < %FILENAME% > %OUTPUT%		
		
	FOR /L %%Y IN (2005,1,2012) DO (
		set FILENAME=%%Y_original.csv		
		set OUTPUT=_%%Y.csv
		java Preprocessamento --remove ; 5 7 8 9 11 12 13 15 16 17 19 20 21 22 23 24 25 26 27 28 29 30 32 33 34 35 37 38 39 40 42 43 44 46 47 48 49 50 52 53 54 55 56 58 59 60 61 62 63 64 66 67 68 69 70 72 73 74 75 76 77 78 79 81 82 83 85 86 87 88 90 91 92 93 95 96 97 99 100 101 103 104 105 106 107 108 110 111 112 113 115 116 117 118 119 120 121 122 124 125 126 128 129 130 131 132 133 135 136 137 138 139 140 142 143 144 146 147 148 149 150 152 153 154 155 156 157 159 160 161 162 164 165 166 167 168 169 170 172< !FILENAME! > !OUTPUT!		
		
	  
	)
	pause	







