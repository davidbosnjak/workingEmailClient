import os
import openai
import sys


openai.api_key = ("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")

response = openai.Completion.create(model="text-davinci-003", prompt=sys.argv[1], temperature=0.7, max_tokens=1200)
text = response['choices'][0]['text']
print(text)