# Cloud Service Firewall Correlator

## Overview

This tool analyzes firewall logs and correlates them with a database of cloud services. It outputs the list of distinct internal IPs that have accessed each cloud service, with support for reverse DNS, IP/user filtering, and concurrency (in later commit).

## Output from the provided csv and log files

```
www.yandex.ru: 211.168.230.94
drive.google.com: 211.168.230.94
www.dropbox.com: 192.150.249.87
www.salesforce.com: 192.150.249.87
aws.amazon.com: 211.168.230.94
```
