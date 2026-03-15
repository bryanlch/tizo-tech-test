# Stage 1: deps
FROM node:20-alpine AS deps
RUN corepack enable
WORKDIR /app

COPY package.json pnpm-lock.yaml* ./
RUN pnpm install

# Stage 2: development
FROM node:20-alpine AS development
RUN corepack enable
WORKDIR /app

COPY --from=deps /app/node_modules ./node_modules
COPY . .

EXPOSE 4200

CMD ["pnpm","start","--host","0.0.0.0","--poll","2000"]