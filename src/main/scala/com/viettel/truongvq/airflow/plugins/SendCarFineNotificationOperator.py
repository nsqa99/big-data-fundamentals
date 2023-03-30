from airflow.models.baseoperator import BaseOperator
import http.client
import json

class SendCarFineNotificationOperator(BaseOperator):
    def __init__(self, car_fine, **kwargs) -> None:
        super().__init__(**kwargs)
        self.car_fine = car_fine

    def execute(self, context):
        conn = http.client.HTTPConnection("onroad-internal.10.240.177.74.nip.io")
        body = json.dumps({"number": "{}".format(self.car_fine[0]),
                           "message": "Ban co {} thong tin phat nguoi".format(self.car_fine[2])})
        print(body)
        headers = {
            "X-Authorization": "secretKey",
            "Content-Type": "application/json"
        }
        conn.request("POST", "/internal-service/onroad-sms/", body, headers)
        res = conn.getresponse()
        print(res.status, res.reason)

