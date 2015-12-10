sc stop LF_BOM
sc delete LF_BOM

set SERVICE_NAME=LF_BOM
set PR_INSTALL=C:\software\prunsrv.exe

REM Service log configuration
set PR_LOGPREFIX=%SERVICE_NAME%
set PR_LOGPATH=C:\local\logs
set PR_STDOUTPUT=C:\local\logs\lf-bom-stdout.log
set PR_STDERROR=C:\local\logs\lf-bom-stderr.log
set PR_LOGLEVEL=Error

REM Path to java installation
set PR_JVM=auto
set PR_CLASSPATH=C:\local\lf-bom\lib\*;C:\local\lf-bom\lib\${project.build.finalName}.jar

REM Startup configuration
set PR_STARTUP=auto
set PR_STARTMODE=jvm
set PR_STARTCLASS=com.lf.bom.launcher.Launcher
set PR_STARTMETHOD=start

REM Shutdown configuration
set PR_STOPMODE=jvm
set PR_STOPCLASS=com.lf.bom.launcher.Launcher
set PR_STOPMETHOD=stop

REM JVM configuration
set PR_JVMMS=256
set PR_JVMMX=1024
set PR_JVMSS=4000
prunsrv.exe //IS//%SERVICE_NAME%