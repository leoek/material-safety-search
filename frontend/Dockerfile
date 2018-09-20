FROM node:9.3.0 as builder
WORKDIR /usr/src/app
COPY package.json yarn.lock ./
RUN yarn
COPY public ./public
COPY src ./src
RUN export CI=true && yarn test && yarn build

FROM nginx:1.13.7
EXPOSE 80
ADD docker/files/default.conf /etc/nginx/conf.d/
COPY ./docker-entrypoint.sh /docker-entrypoint.sh
RUN chmod +x /docker-entrypoint.sh
COPY --from=builder /usr/src/app/build /usr/share/nginx/html
ENTRYPOINT ["/docker-entrypoint.sh"]