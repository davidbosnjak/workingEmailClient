import os
import openai
import sys


openai.api_key = ("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")

response = openai.Completion.create(model="text-davinci-003", prompt=sys.argv[1], temperature=0.7, max_tokens=1200)
text = response['choices'][0]['text']
print(text)