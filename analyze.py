#!/usr/bin/env python3
import re, sys, getopt, os, fnmatch
from collections import Counter

#Input must be a folder.
def main(argv, sname):
    inputFolder = ''
    showPath = False
    regex = '=  ([A-Za-z0-9\s]+)  ='
    try:
        opts, args = getopt.getopt(argv,'hi:r:p',['input=' , 'path', 'regex='])
    except getopt.GetoptError:
        print(sname, '-i <pathToFolder> [-r <regEx>] [-p]')
        sys.exit(2)
    for opt, arg in opts:
        if opt in ("-i", "--input"):
            inputFolder = arg
        if opt in ("-r", "--regex"):
            regex = arg
        if opt in ("-p", "--path"):
            showPath = True

    if inputFolder == '':
        print(sname, '-i <pathToFolder>')
        return
    else:
        print('Input folder is: ' + inputFolder)

    counter = Counter()
    fileCount = 0
    #walk through files
    for root, dirs, files in os.walk(inputFolder):
        #print('Analyzing folder: ' + root, end='\r')
        for fileName in files:
            with open(os.path.join(root, fileName), 'r', encoding = "ISO-8859-1") as f:
                fileString = f.read()
                #=\s\s([A-Za-z0-9\s]+)\s\s=
                found = ''
                found = re.findall(regex, fileString)
                counter.update(found)
                if showPath and found != []:
                    print(os.path.join(root, fileName))
                fileCount += 1
    print('')
    print(fileCount)
    d = {}
    for key, value in counter.items():
        d[key] = value# / fileCount
    print(d)

#cli with arguments: file path, regex (optional)
if __name__ == "__main__":main(sys.argv[1:], sys.argv[0])
