FROM node:16-alpine AS build

# Step 2: Set the working directory inside the container
WORKDIR /app

# Step 3: Copy package.json and package-lock.json (if available) to the working directory
COPY package*.json ./

# Step 4: Install only production dependencies (cached layer)
RUN npm install --production && npm cache clean --force

# Step 5: Copy the rest of the app files into the container
COPY . .

# Step 6: Build the React app
RUN npm run build

# Step 7: Expose the port that the app will run on
EXPOSE 3000

# Step 8: Command to run the app
CMD ["npm", "start"]