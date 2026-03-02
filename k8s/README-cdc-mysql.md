# MySQL CDC-Ready Deployment on Kubernetes

This deployment sets up a MySQL 8.0 instance on Kubernetes with Change Data Capture (CDC) support enabled via binary logging. This is essential for integrating with CDC tools such as Debezium, Maxwell, or custom binlog readers.

## Key Features
- **Binary Logging Enabled**: The deployment configures MySQL with `log-bin`, `binlog-format=ROW`, and a unique `server-id` for CDC compatibility.
- **Custom Configuration**: Uses a ConfigMap to inject a custom `my.cnf` into the MySQL container.
- **Persistent Storage**: Data is stored on a PersistentVolume for durability.
- **Initialization Scripts**: Grants privileges to the application user at startup.

## How It Works
1. **ConfigMap for MySQL Configuration**
   - `mysql-config` contains a `my.cnf` file with:
     ```ini
     [mysqld]
     log-bin=mysql-bin
     binlog-format=ROW
     server-id=1
     ```
   - This enables binary logging and sets the format required for CDC tools.

2. **Deployment**
   - The MySQL container mounts the custom `my.cnf` at `/etc/mysql/conf.d/my.cnf`.
   - Environment variables set up the root password, database, and user credentials.

3. **Persistent Storage**
   - A PersistentVolume and PersistentVolumeClaim ensure data is retained across pod restarts.

4. **Initialization**
   - The `mysql-initdb` ConfigMap runs a script to grant all privileges to the `flowuser` user.

## Usage
1. **Deploy to Kubernetes**
   ```sh
   kubectl apply -f k8s/mysql-deployment.yaml
   ```

2. **Verify Binary Logging**
   - Connect to the MySQL pod:
     ```sh
     kubectl exec -it <mysql-pod-name> -- mysql -u root -p
     ```
   - Run:
     ```sql
     SHOW VARIABLES LIKE 'log_bin';
     SHOW VARIABLES LIKE 'binlog_format';
     SHOW VARIABLES LIKE 'server_id';
     ```
   - All should show the configured values.

3. **Integrate with CDC Tools**
   - Use the MySQL connection details and ensure the CDC tool has access to the binlog (user must have REPLICATION CLIENT/SLAVE privileges if needed).

## Notes
- Adjust `server-id` if deploying multiple MySQL instances for replication.
- Monitor binlog size and retention as needed for your environment.

---

This setup provides a solid foundation for CDC-enabled MySQL workloads on Kubernetes.

