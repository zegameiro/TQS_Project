FROM node:18.17.0-alpine

WORKDIR /customer_app

RUN npm install -g pnpm

COPY . .

RUN pnpm install

EXPOSE 3001

CMD ["pnpm", "dev"]

# FROM nginx:stable-alpine

# COPY --from=build /app/build /usr/share/nginx/html
# COPY --from=build /app/nginx/nginx.conf /etc/nginx/conf.d/default.conf

# EXPOSE 80

# CMD ["nginx", "-g", "daemon off;"]