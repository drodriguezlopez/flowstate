# Manual: Nginx Proxy + Automatic Wildcard SSL (Cloudflare + Certbot)

> ← Back to [Infrastructure Documentation](../README.md)

## 1. Prepare the Cloudflare API Token

In order for Certbot to create DNS records for you, it needs permission:

1. Go to your [Cloudflare Dashboard](https://dash.cloudflare.com/profile/api-tokens).
2. Click on **Create Token** -> Use template **Edit zone DNS**.
3. Under **Zone Resources**, select `your-domain.com`.
4. Copy the generated Token (we will use it in step 3).

---

## 2. Tool Installation

Install Certbot along with the specific plugin for Cloudflare.

```bash
sudo apt update
sudo apt install certbot python3-certbot-nginx python3-certbot-dns-cloudflare -y
```

---

## 3. Configure Cloudflare Credentials

We are going to securely store your token on the server.

1. **Create the directory and the file:**

```bash
sudo mkdir -p /etc/letsencrypt/
sudo nano /etc/letsencrypt/cloudflare.ini
```

2. **Paste the following (replace with your token):**

```ini
dns_cloudflare_api_token = YOUR_CLOUDFLARE_TOKEN_HERE
```

3. **Protect the file (read permissions for root only):**

```bash
sudo chmod 600 /etc/letsencrypt/cloudflare.ini
```

---

## 4. Automatic Wildcard Certificate Acquisition

Now we request the certificate. The plugin will handle communicating with Cloudflare, adding the TXT record, verifying it, and deleting it.

```bash
sudo certbot certonly --dns-cloudflare \
  --dns-cloudflare-credentials /etc/letsencrypt/cloudflare.ini \
  -d "your-domain.com" \
  -d "*.your-domain.com" \
  --preferred-challenges dns-01
```

---

## 5. Nginx Configuration as a Reverse Proxy

### 1. Create the Common Configuration File

First, create a file where we will store the proxy headers and WebSocket configuration.

**Command:** `sudo nano /etc/nginx/snippets/proxy-params.conf`

**Content:**

```nginx
# Standard Proxy Headers
proxy_set_header Host $host;
proxy_set_header X-Real-IP $remote_addr;
proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
proxy_set_header X-Forwarded-Proto $scheme;

# WebSocket Support
proxy_http_version 1.1;
proxy_set_header Upgrade $http_upgrade;
proxy_set_header Connection "upgrade";

# Optimizations
proxy_read_timeout 3600s;
client_max_body_size 0;
```

---

### 2. Create the SSL Configuration File

Since all subdomains use the same Wildcard certificate, let's do the same for the SSL settings.

**Command:** `sudo nano /etc/nginx/snippets/ssl-wildcard.conf`

**Content:**

```nginx
ssl_certificate /etc/letsencrypt/live/your-domain.com/fullchain.pem;
ssl_certificate_key /etc/letsencrypt/live/your-domain.com/privkey.pem;

# Recommended Certbot/Security parameters
ssl_session_cache shared:le_SSL:10m;
ssl_session_timeout 1440m;
ssl_protocols TLSv1.2 TLSv1.3;
ssl_prefer_server_ciphers on;
```

---

### 3. Configure Reverse Proxy to k0s Ingress

Create a new configuration file inside `/etc/nginx/conf.d/` to set up the reverse proxy pointing to the k0s ingress controller.


```nginx
server {
    listen 80;
    server_name *.your-domain.com;
    return 301 https://$host$request_uri;
}

server {
    listen 443 ssl;
    server_name *.your-domain.com;

    # Include SSL settings
    include snippets/ssl-wildcard.conf;

    location / {
        # Forward all traffic to the k0s ingress controller
        proxy_pass https://<k0s-node-ip>;

        # Include proxy parameters
        include snippets/proxy-params.conf;
    }
}
```

Once the file is saved, verify the configuration and reload Nginx:

```bash
sudo nginx -t && sudo systemctl reload nginx
```
