from mpl_toolkits.mplot3d import Axes3D
import time
import numpy as np
from matplotlib.animation import FuncAnimation

import matplotlib.pyplot as plt

file = open("/home/justinas/FRAIM/pointcloudjava.txt", "r")
cloud = file.read()
file.close()

points = cloud.strip().split('\n')

x, y, z = [], [], []

for point in points:
    coords = point.split()
    x.append(float(coords[0]))
    y.append(float(coords[1]))
    z.append(float(coords[2]))

# Create a 3D scatter plot
fig = plt.figure()
ax = fig.add_subplot(111, projection='3d')
sc = ax.scatter(x, y, z)

# Set labels
ax.set_xlabel('X Label')
ax.set_ylabel('Y Label')
ax.set_zlabel('Z Label')

# Set equal scale
max_range = max(max(x) - min(x), max(y) - min(y), max(z) - min(z)) / 2.0
mid_x = (max(x) + min(x)) * 0.5
mid_y = (max(y) + min(y)) * 0.5
mid_z = (max(z) + min(z)) * 0.5

ax.set_xlim(mid_x - max_range, mid_x + max_range)
ax.set_ylim(mid_y - max_range, mid_y + max_range)
ax.set_zlim(mid_z - max_range, mid_z + max_range)

# # Animation function
# def update(frame):
#     ax.view_init(elev=10, azim=frame)
#     plt.pause(0.001)  # Allow the plot to refresh
#     return sc,

# # Create animation
# ani = FuncAnimation(fig, update, frames=np.arange(0, 360, 2), blit=False)  # Set blit to False

# Show the plot
plt.show()
