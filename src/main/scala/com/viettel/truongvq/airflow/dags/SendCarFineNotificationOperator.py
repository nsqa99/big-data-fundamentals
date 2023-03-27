from airflow.models.baseoperator import BaseOperator
import requests


class SendCarFineNotificationOperator(BaseOperator):
    def __init__(self, car_fine, **kwargs) -> None:
        super().__init__(**kwargs)
        self.car_fine = car_fine

    def execute(self, context):
        http_proxy  = "http://10.60.133.139:3128"
        https_proxy = "https://10.60.133.139:3128"
        ftp_proxy   = "ftp://10.60.133.139:3128"

        print(self.car_fine)
        proxies = { 
            "http"  : http_proxy, 
            "https" : https_proxy, 
            "ftp"   : ftp_proxy
        }   
        url = "http://onroad-internal.10.240.177.74.nip.io/internal-service/onroad-sms/"
        body = {"number": "{}".format(self.car_fine[0]), "message": "Ban co {} thong tin phat nguoi".format(self.car_fine[2])},
        headers={"Content-Type": "application/json", "X-Authorization": "secretKey"}
        print(body)

        r = requests.post(url, data=body, headers=headers, proxies=proxies)
        print(r.status_code)
        return r.status_code