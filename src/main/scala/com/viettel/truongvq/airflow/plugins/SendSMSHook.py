from airflow.hooks.base import BaseHook
import http.client
import json


class SendSMSHook(BaseHook):
    def __init__(self, conn_id, *args, **kwargs):
        super().__init__(*args, **kwargs)
        self.conn_id = conn_id

    def send_car_fine_sms(self, car_fine_info):
        conn = self.get_conn()
        body = json.dumps({"number": "{}".format(car_fine_info[0]),
                           "message": "Ban co {} thong tin phat nguoi".format(car_fine_info[2])})
        headers = {
            'X-Authorization': 'secretKey',
            'Content-Type': 'application/json'
        }
        conn.request("POST", "/internal-service/onroad-sms/", body, headers)
        res = conn.getresponse()
        print(res.status, res.reason)

    def get_conn(self):
        conn = self.get_connection(self.conn_id)
        host = conn.host
        connection_to_sms_server = http.client.HTTPConnection(host)
        return connection_to_sms_server


