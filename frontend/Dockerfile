# Step 1: Build the Angular app
FROM node:18-alpine AS builder

WORKDIR /app

COPY package*.json ./
RUN npm install --force

COPY . .

RUN npx ng build untitled5 --configuration production

# Step 2: Serve with NGINX
FROM nginx:alpine

COPY --from=builder /app/dist/untitled5 /usr/share/nginx/html

EXPOSE 80

CMD ["nginx", "-g", "daemon off;"]
