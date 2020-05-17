import datetime
import re

import jalali
import mysql.connector
import xlrd


def exists_in_table(table_name, table_field, value):
    sql_statement = "SELECT id FROM " + table_name + " WHERE " + table_field + " = '" + value + "'"
    try:
        cursor.execute(sql_statement)
        result = cursor.fetchone()  # None, cursor.fetchone()[0] --> id
        if not result:
            return -1
        else:
            return result[0]
    except mysql.connector.IntegrityError as e:
        db_connection.rollback()
        print("user error: ", e)
        return -1


def add_user():
    sql_statement = "INSERT INTO users (name, active, tel_number, mobile, email, username, nationalid, password) " \
                    "VALUES (%s, %s, %s, %s, %s, %s, %s, %s)"
    values = (u_name, "Y", u_tel, u_mobile, u_email, u_username, u_nationalid,
              "$2a$04$6BCGBO9fPSolYJgEiPHYeOpuNlvqgYUOsQCeBqZuyjGOfnqhGADgu")
    try:
        cursor.execute(sql_statement, values)
        db_connection.commit()
        return cursor.lastrowid

    except mysql.connector.IntegrityError as e:
        db_connection.rollback()
        print("user error: ", e)
        return -1


def add_user_role():
    sql_statement = "INSERT INTO user_roles (user_id, role_id) VALUES (%s, %s)"
    values = (user_id, 2)
    try:
        cursor.execute(sql_statement, values)
        db_connection.commit()
        return cursor.lastrowid

    except mysql.connector.IntegrityError as e:
        db_connection.rollback()
        print("user_role error: ", e)
        return -1


def add_technical_person():
    sql_statement = "INSERT INTO technical_persons (name, tel_number, mobile, email, national_id, user_id) " \
                    "VALUES (%s, %s, %s, %s, %s, %s)"
    values = (tec_name, tec_tel, tec_mobile, tec_email, tec_national_id, user_id)
    try:
        cursor.execute(sql_statement, values)
        db_connection.commit()
        return cursor.lastrowid
    except mysql.connector.IntegrityError as e:
        db_connection.rollback()
        print("tec_person error: ", e)
        return -1


def add_company():
    sql_statement = "INSERT INTO company (active, financialid, name, nationalid, " \
                    "registrationid, tel_number, type, user_id) " \
                    "VALUES (%s, %s, %s, %s, %s, %s, %s, %s)"
    values = ("Y", company_financial_id, company_name, company_national_id,
              company_registration_id, company_tel_number, company_type_int, user_id)

    try:
        cursor.execute(sql_statement, values)
        db_connection.commit()
        return cursor.lastrowid

    except mysql.connector.IntegrityError as e:
        db_connection.rollback()
        print("company error: ", e)
        return -1


def add_os():
    sql_statement = "INSERT INTO os_types (name, description, active) " \
                    "VALUES (%s, %s, %s)"
    values = (os_type_name, "", "Y")
    try:
        cursor.execute(sql_statement, values)
        db_connection.commit()
        return cursor.lastrowid
    except mysql.connector.IntegrityError as e:
        db_connection.rollback()
        print("os_type error: ", e)
        return -1


def add_vps_plan(vps_plan_name, vps_plan_cpu, vps_plan_ram, vps_plan_storage, max_traffic,
                 vps_plan_monthly_price):
    sql_statement = "INSERT INTO vps_plans (active, name, cpu, ram, disk, max_traffic, monthly_price, " \
                    "free_traffic) " \
                    "VALUES (%s, %s, %s, %s, %s, %s, %s, %s)"
    values = ("Y", vps_plan_name, vps_plan_cpu, vps_plan_ram, vps_plan_storage, max_traffic,
              vps_plan_monthly_price, 0)
    try:
        cursor.execute(sql_statement, values)
        db_connection.commit()
        return cursor.lastrowid

    except mysql.connector.IntegrityError as e:
        db_connection.rollback()
        print("vps_plan error: ", e)
        return -1


def add_service():
    sql_statement = "INSERT INTO services (id, create_date, active, name, description, company_id, os_type_id, technical_person_id, " \
                    "vps_plan_id, service_type, start_date, type, discount_percent, extra_cpu, extra_disk," \
                    "extra_ram, extra_traffic, price, final_price, unit_number, valid_ip, invalid_ip, vnc, duration, extra_price, " \
                    "status) " \
                    "VALUES (%s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, " \
                    "%s)"
    values = (service_id, service_create_date, "Y", service_name, service_description, company_id, os_type_id,
              technical_person_id,
              vps_plan_id, 1,
              service_start_date, vps_type, service_discount_percent, vps_plan_extra_cpu, vps_plan_extra_storage,
              vps_plan_extra_ram, 0, service_price, service_final_price, 0, service_valid_ip, service_invalid_ip, "N",
              service_duration, service_extra_price, 5)
    try:
        cursor.execute(sql_statement, values)
        db_connection.commit()
        return cursor.lastrowid

    except mysql.connector.IntegrityError as e:
        db_connection.rollback()
        print("service error: ", e)
        return -1


def add_colocation_service():
    sql_statement = "INSERT INTO services (id, create_date, active, name, description, company_id, os_type_id, technical_person_id, " \
                    "vps_plan_id, service_type, start_date, type, discount_percent, extra_cpu, extra_disk," \
                    "extra_ram, extra_traffic, price, final_price, unit_number, valid_ip, invalid_ip, vnc, duration, extra_price, " \
                    "status, sla_type) " \
                    "VALUES (%s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, " \
                    "%s)"
    values = (service_id, service_create_date, "Y", service_name, service_description, company_id, 1,
              technical_person_id,
              None, 0,
              service_start_date, colocation_type, service_discount_percent, 0, 0,
              0, 0, service_price, service_final_price, service_unitNumber, service_valid_ip, service_invalid_ip, "N",
              service_duration, service_extra_price, 5, service_slaType)
    try:
        cursor.execute(sql_statement, values)
        db_connection.commit()
        return cursor.lastrowid

    except mysql.connector.IntegrityError as e:
        db_connection.rollback()
        print("service error: ", e)
        return -1


def add_port():
    sql_statement = "INSERT INTO ports (port, service_id) " \
                    "VALUES (%s, %s)"
    values = (port.strip(), service_id)
    try:
        cursor.execute(sql_statement, values)
        db_connection.commit()
        return cursor.lastrowid

    except mysql.connector.IntegrityError as e:
        db_connection.rollback()
        print("port error: ", e)
        return -1


def add_ip():
    sql_statement = "INSERT INTO ips (ip, service_id) " \
                    "VALUES (%s, %s)"
    values = (ip, service_id)
    try:
        cursor.execute(sql_statement, values)
        db_connection.commit()
        return cursor.lastrowid

    except mysql.connector.IntegrityError as e:
        db_connection.rollback()
        print("ip error: ", e)
        return -1


def find_user_row(value, sheet_name, column):
    for searchIndex in range(1, sheet_name.nrows):
        if int(sheet_name.cell_value(searchIndex, column)) == int(value):
            return searchIndex
    return -1


def diff_month(d1, d2):
    return (d1.year - d2.year) * 12 + d1.month - d2.month


def ip_numbers(ips):
    ipNumber = 0
    ips_splited = ips.splitlines()
    for ip in ips_splited:
        if '-' in ip:
            a = re.findall(r'\d{1,3}\-\d{1,3}', ip)
            b = a[0].split('-')
            ipNumber += int(b[1]) - int(b[0]) + 1
        elif '/' in ip:
            a = ip.replace(' ', '/').split('/')
            c = 2 ** (32 - int(a[1]))
            if len(a) > 2:
                c += 2 ** (32 - int(a[3]))
            ipNumber += c
        else:
            ipNumber += int(str(ip).count('.') / 3)
    return ipNumber


loc = ("4.xlsx")

wb = xlrd.open_workbook(loc)
sheet = wb.sheet_by_index(1)

colocation_sheet = wb.sheet_by_index(2)
vps_sheet = wb.sheet_by_index(3)
os_sheet = wb.sheet_by_index(9)

db_connection = mysql.connector.connect(
    host="localhost",
    user="netmon",
    passwd="netmonpass",
    database="netmon_app")
cursor = db_connection.cursor()

if exists_in_table("vps_plans", "name", "سرور خالی") == -1:
    print("ADD VPS_PLAN: سرور خالی")
    add_vps_plan("سرور خالی", 0, 0, 0, 0, 0)
if exists_in_table("vps_plans", "name", "سرور کمینه") == -1:
    print("ADD VPS_PLAN: سرور کمینه")
    add_vps_plan("سرور کمینه", 2, 2, 80, 200, 1100000)
if exists_in_table("vps_plans", "name", "سرور پایه") == -1:
    print("ADD VPS_PLAN: سرور پایه")
    add_vps_plan("سرور پایه", 4, 4, 120, 300, 1900000)
if exists_in_table("vps_plans", "name", "سرور متوسط") == -1:
    print("ADD VPS_PLAN: سرور متوسط")
    add_vps_plan("سرور متوسط", 6, 6, 160, 400, 2700000)
if exists_in_table("vps_plans", "name", "سرور پیشرفته") == -1:
    print("ADD VPS_PLAN: سرور پیشرفته")
    add_vps_plan("سرور پیشرفته", 8, 8, 240, 500, 3700000)

for i in range(1, sheet.nrows):
    # user
    u_name = sheet.cell_value(i, 3) + " " + sheet.cell_value(i, 4)
    u_tel = sheet.cell_value(i, 7)
    u_mobile = sheet.cell_value(i, 8)
    u_email = sheet.cell_value(i, 9)
    u_username = sheet.cell_value(i, 1)
    u_nationalid = sheet.cell_value(i, 6)
    # if u_nationalid == '':
    #     print(u_username)
    #     print('*******')

    user_id = exists_in_table("users", "username", u_username)
    if user_id == -1:
        print("ADD USER: ", u_username)
        user_id = add_user()
        add_user_role()
    # technical-person
    #    tec_name = sheet.cell_value(i, 8) + " " + sheet.cell_value(i, 9)
    #    tec_national_id = int(sheet.cell_value(i, 11))
    #    tec_tel = sheet.cell_value(i, 13)
    #    tec_mobile = sheet.cell_value(i, 12)
    #    tec_email = sheet.cell_value(i, 10)

    # company
    company_type = sheet.cell_value(i, 10)
    if company_type == 'دانشگاهی':
        company_type_int = '0'
    elif company_type == 'شرکت':
        company_type_int = '2'
    else:
        company_type_int = '1'
    company_name = sheet.cell_value(i, 12)
    company_national_id = sheet.cell_value(i, 13)
    company_financial_id = sheet.cell_value(i, 14)
    company_registration_id = sheet.cell_value(i, 15)
    company_tel_number = sheet.cell_value(i, 16)

    company_id = exists_in_table("company", "name", company_name)
    if company_id == -1:
        print("ADD COMPANY: ", company_name)
        company_id = add_company()

#    ports = sheet.cell_value(i, 26)
#    if ports:
#        port_split = str(ports).split('-')
##        for port in port_split:
#            add_port()
#
#    ip = sheet.cell_value(i, 24)
#    add_ip()
#
#    ip = sheet.cell_value(i, 25)
#    add_ip()

# vps contracts
for i in range(1, vps_sheet.nrows):
    service_id = i
    # vps-plan
    # vps_plan_id = int(sheet.cell_value(i, 34))
    # vps_plan_id = exists_in_table("vps_plans", "name", vps_sheet.cell_value(i, 24))
    vps_plan_extra_cpu = vps_sheet.cell_value(i, 25)
    if vps_plan_extra_cpu == '':
        vps_plan_extra_cpu = 0
    vps_plan_extra_ram = vps_sheet.cell_value(i, 26)
    if vps_plan_extra_ram == '':
        vps_plan_extra_ram = 0
    vps_plan_extra_storage = vps_sheet.cell_value(i, 27)
    if vps_plan_extra_storage == '':
        vps_plan_extra_storage = 0

    # service
    service_name = vps_sheet.cell_value(i, 11)
    if vps_sheet.cell_value(i, 18):
        service_start_date = jalali.Persian("13" + vps_sheet.cell_value(i, 18)).gregorian_string()
    service_duration = 36
    service_create_date = service_start_date
    if vps_sheet.cell_value(i, 19) != '':
        service_duration = int(vps_sheet.cell_value(i, 19))
    service_description = (vps_sheet.cell_value(i, 2)) + " " + str(vps_sheet.cell_value(i, 12))
    service_discount_percent = vps_sheet.cell_value(i, 22)
    if service_discount_percent == '':
        service_discount_percent = 0
    service_final_price = 0
    if vps_sheet.cell_value(i, 29) != '':
        service_final_price = float(vps_sheet.cell_value(i, 29)) / 12
    service_price = 0
    if vps_sheet.cell_value(i, 28) != '':
        service_price = float(vps_sheet.cell_value(i, 28)) / 12
    service_extra_price = vps_sheet.cell_value(i, 23)
    if service_extra_price == '':
        service_extra_price = 0
    if vps_sheet.cell_value(i, 15) != "":
        service_valid_ip = "1"
    else:
        service_valid_ip = "0"

    service_invalid_ip = "1"

    # technical-person
    tec_name = vps_sheet.cell_value(i, 3) + " " + vps_sheet.cell_value(i, 4)
    tec_national_id = int(vps_sheet.cell_value(i, 6))
    tec_tel = vps_sheet.cell_value(i, 8)
    tec_mobile = vps_sheet.cell_value(i, 7)
    tec_email = vps_sheet.cell_value(i, 5)
    # user_row = str(int(vps_sheet.cell_value(i, 1)))
    u_username = ''
    user_row = find_user_row(vps_sheet.cell_value(i, 1), sheet, 0)
    u_username = sheet.cell_value(user_row, 1)
    company_name = sheet.cell_value(user_row, 12)
    # for j in range(1, sheet.nrows):
    #     if str(int(sheet.cell_value(j, 0))) == user_row:
    #         u_username = sheet.cell_value(j, 1)
    #         company_name = sheet.cell_value(j, 12)
    user_id = exists_in_table('users', 'username', u_username)
    company_id = exists_in_table("company", "name", company_name)

    technical_person_id = exists_in_table("technical_persons", "email", str(tec_email))
    if technical_person_id == -1:
        print("ADD TECHNICAL-PERSON: ", tec_national_id)
        technical_person_id = add_technical_person()

    # os-type
    os_type = int(vps_sheet.cell_value(user_row, 17))
    os_type_name = os_sheet.cell_value(os_type - 1, 1)
    os_type_id = exists_in_table("os_types", "name", os_type_name)
    if os_type_id == -1:
        print("ADD OS-TYPE: ", os_type_name)
        os_type_id = add_os()

    # vps type
    vps_type = 2
    vps_plan_id = int(vps_sheet.cell_value(i, 24)) + 1
    if vps_sheet.cell_value(i, 10) == 'ستادی':
        vps_type = 1
    service_id = add_service()

for colocation_index in range(1, colocation_sheet.nrows):
    service_id = i + colocation_index
    if colocation_sheet.cell_value(colocation_index, 13):
        service_start_date = jalali.Persian(colocation_sheet.cell_value(colocation_index, 13)).gregorian_string()
    service_create_date = service_start_date
    service_end_date = jalali.Persian(colocation_sheet.cell_value(colocation_index, 14)).gregorian_string()
    service_description = (colocation_sheet.cell_value(colocation_index, 2)) + " " + str(
        colocation_sheet.cell_value(colocation_index, 12))

    d1 = datetime.datetime.strptime(service_start_date, "%Y-%m-%d")
    d2 = datetime.datetime.strptime(service_end_date, "%Y-%m-%d")
    service_duration = diff_month(d2, d1)
    user_row = find_user_row(colocation_index, colocation_sheet, 1)
    service_name = colocation_sheet.cell_value(colocation_index, 2)
    service_description = colocation_sheet.cell_value(colocation_index, 2)

    company_name = sheet.cell_value(user_row, 12)
    company_id = exists_in_table("company", "name", company_name)
    if company_id == -1:
        print("ADD COMPANY: ", company_name)
        company_id = add_company()
    colocation_type = 2
    service_unitNumber = int(colocation_sheet.cell_value(colocation_index, 4))
    ips = colocation_sheet.cell_value(colocation_index, 11)
    ipNumber = ip_numbers(ips)
    # add_ip()
    if colocation_sheet.cell_value(colocation_index, 3) == 'ستادی':
        colocation_type = 1
        service_discount_percent = 0
        service_price = 0
        service_final_price = 0
    else:
        service_discount_percent = colocation_sheet.cell_value(colocation_index, 9)
        unit_price = int(colocation_sheet.cell_value(colocation_index, 15))
        ip_price = int(colocation_sheet.cell_value(colocation_index, 16))
        service_price = unit_price * service_unitNumber + ip_price * ipNumber
        service_extra_price = colocation_sheet.cell_value(colocation_index, 10)
        service_final_price = service_price * service_duration
    service_slaType = colocation_sheet.cell_value(colocation_index, 5)
    if colocation_sheet.cell_value(colocation_index, 11) != "":
        service_valid_ip = "1"
    else:
        service_valid_ip = "0"
    if colocation_sheet.cell_value(colocation_index, 12) != "":
        service_invalid_ip = "1"
    else:
        service_invalid_ip = "0"

    # technical-person
    tec_name = colocation_sheet.cell_value(colocation_index, 21) + " " + colocation_sheet.cell_value(colocation_index,
                                                                                                     22)
    tec_national_id = int(colocation_sheet.cell_value(colocation_index, 23))
    tec_tel = colocation_sheet.cell_value(colocation_index, 24)
    tec_mobile = colocation_sheet.cell_value(colocation_index, 25)
    tec_email = colocation_sheet.cell_value(colocation_index, 26)
    technical_person_id = exists_in_table("technical_persons", "email", str(tec_email))
    if technical_person_id == -1:
        print("ADD TECHNICAL-PERSON: ", tec_national_id)
        technical_person_id = add_technical_person()
    print(colocation_index)
    add_colocation_service()
    # if colocation_sheet.cell_value(colocation_index, 28) != '':
    #     service_price = float(vps_sheet.cell_value(i, 28)) / 12
    # service_extra_price = vps_sheet.cell_value(i, 23)
    # if service_extra_price == '':
    #     service_extra_price = 0
    # if vps_sheet.cell_value(i, 15) != "":
    #     service_valid_ip = "1"
    # else:
    #     service_valid_ip = "0"

    # service_invalid_ip = "1"
