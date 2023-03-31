import http.client
import json
import ssl
from datetime import datetime


def main():
  dt_string = datetime.strptime("2023-03-30T03:00:10+00:00", "%Y-%m-%dT%H:%M:%S%z").g
  print("date and time =", dt_string.time())



if __name__ == '__main__':
    main()
