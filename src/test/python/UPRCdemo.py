import requests
from base64 import b64encode

#BASE = 'http://localhost:8080'
#BASE = 'https://lightest-dev.iaik.tugraz.at/atvapi'
BASE = 'https://atvapi.tug.do.nlnetlabs.nl/atvapi/'


POLICY_FILE      = '../../../policy_UPRC.tpl'
TRANSACTION_FILE = '../../../cert_UPRC.pem'


if requests.get(BASE + '/api/v1/').json()['result'] != 1:
    print('Wrong BASE?')


payload = dict()
payload['policy']      = b64encode(open(POLICY_FILE, 'rb').read()).decode('utf-8')
payload['transaction'] = b64encode(open(TRANSACTION_FILE, 'rb').read()).decode('utf-8')

#print(payload)

resp = requests.post(BASE + '/api/v1/addInstance', json=payload)

print(resp.text)

for line in resp.json()['report']:
    print(line)
