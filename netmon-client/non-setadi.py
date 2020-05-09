import mysql.connector
import xlrd
import jalali

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
              "$2a$10$oamUG1evEoHowDHdsXauWerUBTD5HBAZPmayyKJ..dKwTWSULMR42")
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
    values = (os_type, "", "Y")
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
    sql_statement = "INSERT INTO services (active, name, description, company_id, os_type_id, technical_person_id, " \
                    "vps_plan_id, service_type, start_date, type, discount_percent, extra_cpu, extra_disk," \
                    "extra_ram, extra_traffic, price, final_price, unit_number, valid_ip, vnc, duration, extra_price, " \
                    "status) " \
                    "VALUES (%s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, " \
                    "%s)"
    values = ("Y", service_name, service_description, company_id, os_type_id, technical_person_id, vps_plan_id, 1,
              service_start_date, 2, service_discount_percent, vps_plan_extra_cpu, vps_plan_extra_storage,
              vps_plan_extra_ram, 0, service_price, service_final_price, 0, service_valid_ip, "N",
              service_duration, service_extra_price, 5)
    try:
        cursor.execute(sql_statement, values)
        db_connection.commit()
        return cursor.lastrowid

    except mysql.connector.IntegrityError as e:
        db_connection.rollback()
        print("vps_plan_id: ", vps_plan_id)
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

loc = ("2.5/VPS-v2.5-Nonsetadi.xlsx")

wb = xlrd.open_workbook(loc)
sheet = wb.sheet_by_index(0)

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

for i in range(3, sheet.nrows):

    # user
    u_name = sheet.cell_value(i, 1) + " " + sheet.cell_value(i, 2)
    u_tel = sheet.cell_value(i, 7)
    u_mobile = sheet.cell_value(i, 6)
    u_email = sheet.cell_value(i, 3)
    u_username = sheet.cell_value(i, 4)
    u_nationalid = sheet.cell_value(i, 5)

    # technical-person
    tec_name = sheet.cell_value(i, 8) + " " + sheet.cell_value(i, 9)
    tec_national_id = int(sheet.cell_value(i, 11))
    tec_tel = sheet.cell_value(i, 13)
    tec_mobile = sheet.cell_value(i, 12)
    tec_email = sheet.cell_value(i, 10)

    # company
    company_type = sheet.cell_value(i, 14)
    if company_type == 'دانشگاهی':
        company_type_int = '0'
    elif company_type == 'شرکت':
        company_type_int = '2'
    else:
        company_type_int = '1'
    company_name = sheet.cell_value(i, 15)
    company_national_id = sheet.cell_value(i, 17)
    company_financial_id = sheet.cell_value(i, 18)
    company_registration_id = sheet.cell_value(i, 19)
    company_tel_number = sheet.cell_value(i, 20)

    # os-type
    os_type = sheet.cell_value(i, 27)

    # vps-plan
    # vps_plan_id = int(sheet.cell_value(i, 34))
    vps_plan_id = exists_in_table("vps_plans", "name", sheet.cell_value(i, 34))
    vps_plan_extra_cpu = sheet.cell_value(i, 35)
    vps_plan_extra_ram = sheet.cell_value(i, 36)
    vps_plan_extra_storage = sheet.cell_value(i, 37)

    # service
    service_name = sheet.cell_value(i, 23)
    if sheet.cell_value(i, 28):
        service_start_date = jalali.Persian("13" + sheet.cell_value(i, 28)).gregorian_string()
    service_duration = sheet.cell_value(i, 29)
    service_description = sheet.cell_value(i, 0) + " " + sheet.cell_value(i, 16)
    service_discount_percent = sheet.cell_value(i, 32)
    service_final_price = float(sheet.cell_value(i, 40)) / 12
    service_price = float(sheet.cell_value(i, 39)) / 12
    service_extra_price = sheet.cell_value(i, 33)
    if sheet.cell_value(i, 38) == "yes":
        service_valid_ip = "Y"
    else:
        service_valid_ip = "N"

    user_id = exists_in_table("users", "username", u_username)
    if user_id == -1:
        print("ADD USER: ", u_username)
        user_id = add_user()
        add_user_role()

    technical_person_id = exists_in_table("technical_persons", "national_id", str(tec_national_id))
    if technical_person_id == -1:
        print("ADD TECHNICAL-PERSON: ", tec_national_id)
        technical_person_id = add_technical_person()

    company_id = exists_in_table("company", "name", company_name)
    if company_id == -1:
        print("ADD COMPANY: ", company_name)
        company_id = add_company()

    os_type_id = exists_in_table("os_types", "name", os_type)
    if os_type_id == -1:
        print("ADD OS-TYPE: ", os_type)
        os_type_id = add_os()

    print("ADD SERVICE: ", service_name)
    service_id = add_service()

    ports = sheet.cell_value(i, 26)
    if ports:
        port_split = str(ports).split('-')
        for port in port_split:
            add_port()

    ip = sheet.cell_value(i, 24)
    add_ip()

    ip = sheet.cell_value(i, 25)
    add_ip()