# Build Stage
FROM node:14 AS build

WORKDIR /app
COPY package*.json /app/

# Install Angular CLI globally
RUN npm install -g @angular/cli

# Install dependencies
RUN npm install --legacy-peer-deps

COPY ./ /app/

# Run the build using the Angular CLI
RUN ng build --configuration=production --output-path=dist

# Run Stage
FROM nginx:1.21-alpine as production-stage

# Copy the built files from the previous stage into the NGINX server
COPY --from=build /app/dist /usr/share/nginx/html

EXPOSE 4200

CMD ["nginx", "-g", "daemon off;"]
