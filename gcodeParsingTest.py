import matplotlib.pyplot as plt

file = open("/home/justinas/FRAIM/model.gcode", "r")
gcode = file.read()
file.close()

pointCloud = open("/home/justinas/FRAIM/pointcloud.txt", "w")

xarray = []
yarray = []

z = 0

def addLine(subString):
    #x
    x = 0
    xI = 0
    for char in subString:
        if char == "X":
            innerLoopIndex = xI
            while gcode[innerLoopIndex] != " ":
                innerLoopIndex = innerLoopIndex + 1
            x = subString[xI + 1 :innerLoopIndex]
            xarray.append(x)
        xI = xI + 1

    #y
    y = 0
    yI = 0
    for char in subString:
        if char == "Y":
            innerLoopIndex = yI
            while gcode[innerLoopIndex] != " ":
                innerLoopIndex = innerLoopIndex + 1
            y = subString[yI + 1 :yI + 6]
            yarray.append(y)
        yI = yI + 1

    pointCloud.write(x + ", " + y + "\n")


count = 0
zcount = 1
for char in gcode:

    if gcode[count] == "G" and gcode[count+1] == "1" and gcode[count + 3] == "X":

        innerLoopIndex = count
        while gcode[innerLoopIndex] != "\n":
            innerLoopIndex = innerLoopIndex + 1
        
        addLine(gcode[count + 0 : innerLoopIndex])

    if gcode[count] == "G" and gcode[count+1] == "1" and gcode[count + 3] == "Z":
        #print(zcount)
        zcount = zcount + 1

    count = count + 1


#plt.plot([2,5.4,3,2],[1,3,7,6],'o')
# print(xarray)
# print(yarray)
# xarray.sort()
# yarray.sort()
# plt.plot(xarray,yarray,'o')



plt.xlabel("X-axis")
plt.ylabel("Y-axis")
#plt.grid(True)
# plt.xlim([100,150])
# plt.ylim([80,120])
#plt.xscale(1)
#plt.yscale(1)

plt.show()