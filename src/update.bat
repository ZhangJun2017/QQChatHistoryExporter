@echo off
protoc -I=. --java_out=. RichMsgHandle.proto
pause