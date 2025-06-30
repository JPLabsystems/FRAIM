from mpl_toolkits.mplot3d import Axes3D
import numpy as np

import matplotlib.pyplot as plt

file = open("/home/justinas/FRAIM/Clouds/pointcloud1.txt", "r")
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
ax.set_xlabel('X')
ax.set_ylabel('Y')
ax.set_zlabel('Z')

# Set equal scale
max_range = max(max(x) - min(x), max(y) - min(y), max(z) - min(z)) / 2.0
mid_x = (max(x) + min(x)) * 0.5
mid_y = (max(y) + min(y)) * 0.5
mid_z = (max(z) + min(z)) * 0.5

ax.set_xlim(mid_x - max_range, mid_x + max_range)
ax.set_ylim(mid_y - max_range, mid_y + max_range)
ax.set_zlim(mid_z - max_range, mid_z + max_range)

# --- Drop-in segment to draw the perpendicular rectangles ---

# Define the bounding box coordinates for the rectangles
x_min_box, x_max_box = 89.22, 161.36
y_min_box, y_max_box = 94.35, 114.82
z_min_box, z_max_box = 0.20, 13.80

# Calculate midpoints for the intersection of the rectangles
y_mid_box = (y_min_box + y_max_box) / 2
x_mid_box = (x_min_box + x_max_box) / 2

# Rectangle 1: Vertical, in the X-Z plane, positioned at y_mid_box
# This rectangle spans the X and Z dimensions at a fixed Y slice.
# Vertices are defined to close the loop (first point repeated at the end).
rect1_x = [x_min_box, x_max_box, x_max_box, x_min_box, x_min_box]
rect1_y = [y_mid_box, y_mid_box, y_mid_box, y_mid_box, y_mid_box]
rect1_z = [z_min_box, z_min_box, z_max_box, z_max_box, z_min_box]

# Plot Rectangle 1
ax.plot(rect1_x, rect1_y, rect1_z, color='red', linestyle='-', linewidth=1, label='Rectangle 1 (X-Z plane)')

# Rectangle 2: Vertical, in the Y-Z plane, positioned at x_mid_box
# This rectangle spans the Y and Z dimensions at a fixed X slice.
# Vertices are defined to close the loop.
rect2_x = [x_mid_box, x_mid_box, x_mid_box, x_mid_box, x_mid_box]
rect2_y = [y_min_box, y_max_box, y_max_box, y_min_box, y_min_box]
rect2_z = [z_min_box, z_min_box, z_max_box, z_max_box, z_min_box]

# Plot Rectangle 2
ax.plot(rect2_x, rect2_y, rect2_z, color='red', linestyle='-', linewidth=1, label='Rectangle 2 (Y-Z plane)')

# Add a legend to the plot to identify the rectangles
ax.legend()

# Add a title to the plot for overall clarity
ax.set_title('pointcloud')

# Plot a single red point at (1, 1, 1)
ax.scatter(0, 0, 0, color='red', s=50)

# Create a unit sphere
u = np.linspace(0, 2 * np.pi, 100)
v = np.linspace(0, np.pi, 100)
x_sphere = np.outer(np.cos(u), np.sin(v))
y_sphere = np.outer(np.sin(u), np.sin(v))
z_sphere = np.outer(np.ones(np.size(u)), np.cos(v))

# Plot the sphere

# ax.plot_surface(x_sphere, y_sphere, z_sphere, color='blue', alpha=0.2)

# # Animation function
# def update(frame):
#     ax.view_init(elev=10, azim=frame)
#     plt.pause(0.001)  # Allow the plot to refresh
#     return sc,

# # Create animation
# ani = FuncAnimation(fig, update, frames=np.arange(0, 360, 2), blit=False)  # Set blit to False

# Show the plot
plt.show()
