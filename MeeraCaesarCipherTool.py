"""

Meera Kabilan
mkab129
Caesar Cipher Tool

"""

def main():
    print_banner()
    print()
    perform_user_selection()
    
def perform_user_selection():
    user_selection = get_user_selection()
    while user_selection != "STOP":
        if user_selection == "E":
            message = input("Enter the message to be encrypted: ")
            key = get_key()
            encrypted_message = encrypt_message(message, key)
            print(f"The encrypted message is: {encrypted_message}")
        else:
            message = input("Enter the message to be decrypted: ")
            key = get_key()
            decrypted_message = decrypt_message(message, key)
            print(f"The decrypted message is: {decrypted_message}")
        print()
        user_selection = get_user_selection()
        
def print_banner():
    print("**********************")
    print("* Caesar cipher tool *")
    print("**********************")

def get_user_selection():
    prompt = "Enter E for encrypting, D for decrypting or STOP to exit: "
    selection = input(prompt)
    while selection not in ("D", "E", "STOP"):
        print("Incorrect selection.")
        selection = input(prompt)
    return selection

def get_key():
    key = int(input("Enter the key: "))
    return key

def encrypt_message(message, key):
    alphabet = "abcdefghijklmnopqrstuvwxyz"
    encrypted_message = ""
    for char in message:
        if char.lower() in alphabet:
            cipher_index = (alphabet.index(char.lower()) + key) % 26
            if char.isupper():
                encrypted_message += alphabet[cipher_index].upper()
            else:
                encrypted_message += alphabet[cipher_index]
        else:
            encrypted_message += char
    return encrypted_message

def decrypt_message(message, key):
    negative_key = key * -1
    decrypted_message = encrypt_message(message, negative_key)
    return decrypted_message

main()
