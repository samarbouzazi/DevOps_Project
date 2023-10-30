# # Build Stage
# FROM node:16 AS build

# WORKDIR /app
# COPY package*.json /app/

# # Install Angular CLI globally
# RUN npm install -g @angular/cli

# # Install dependencies
# RUN npm install --legacy-peer-deps

# COPY ./ /app/

# # Run the build using the Angular CLI
# RUN ng build --configuration=production --output-path=dist

# # Run Stage
# FROM nginx:1.21-alpine as production-stage

# # Copy the built files from the previous stage into the NGINX server
# COPY --from=build /app/dist /usr/share/nginx/html

# EXPOSE 4200

# CMD ["nginx", "-g", "daemon off;"]


# Stage 1: Build the Angular Application
FROM node:16 AS build

WORKDIR /usr/src/app

# Copy package.json and package-lock.json
COPY package*.json ./

# Install dependencies
RUN npm install --legacy-peer-deps

# Copy the rest of the application code
COPY . .

# Build the Angular app
RUN npm run build -- --prod

# Stage 2: Serve the Application with Nginx
FROM nginx:alpine

# Remove default Nginx website
RUN rm -rf /usr/share/nginx/html/*

# Copy the Angular app from the previous build stage
COPY --from=build /usr/src/app/dist/SummerWorkshop_Angular /usr/share/nginx/html

# Expose port 80
EXPOSE 80

# Start Nginx
CMD ["nginx", "-g", "daemon off;"]
