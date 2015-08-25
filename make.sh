#!/bin/bash

cd source && javac -source 1.7 -target 1.7 -d ../classes com/ict/algo/*.java

cd ../classes && jar -cvmf manifest.txt ~/desktop/BSplineCurve.jar com
